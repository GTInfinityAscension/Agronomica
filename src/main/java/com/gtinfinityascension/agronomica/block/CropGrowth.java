package com.gtinfinityascension.agronomica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.item.SeedItem;

/** Growth and harvest rules, kept apart from the block so tests can call them directly. */
public final class CropGrowth {

    public static final int MIN_LIGHT = 9;

    private CropGrowth() {}

    /**
     * EN: Growth roll per random tick: 35 % at Growth 1 up to ~97 % at 31 (vanilla wheat is ~33 % on farmland).
     * PT: Chance de crescer por random tick: 35 % com Crescimento 1 até ~97 % com 31 (trigo vanilla ≈ 33 %).
     * ZH: 每次随机刻的生长概率：生长 1 时 35%，31 时约 97%（原版小麦在耕地上约 33%）。
     */
    public static float growthChance(CropStats stats) {
        return 0.35f + CropStats.effective(stats.growth()) * 0.025f;
    }

    /** N/P/K a step costs: Resistance cuts the demand, up to about 40 % at 31. */
    public static int scaledDemand(int demand, CropStats stats) {
        return Math.max(0, Math.round(demand * (1 - CropStats.effective(stats.resistance()) / 62f)));
    }

    public static boolean tick(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (level.getRawBrightness(pos, 0) < MIN_LIGHT) return false;
        if (!(level.getBlockEntity(pos) instanceof AgCropBlockEntity crop)) return false;
        if (random.nextFloat() >= growthChance(crop.stats())) return false;
        return growNow(level, pos, state, crop);
    }

    /** One step if the soil can pay for it; returns false when mature or starved. */
    public static boolean growNow(ServerLevel level, BlockPos pos, BlockState state, AgCropBlockEntity crop) {
        int age = state.getValue(AgCropBlock.AGE);
        if (age >= AgCropBlock.MAX_AGE) return false;
        var s = crop.species();
        var stats = crop.stats();
        if (!crop.nutrients().consume(scaledDemand(s.demandN(), stats), scaledDemand(s.demandP(), stats),
                scaledDemand(s.demandK(), stats)))
            return false;
        crop.setChanged();
        level.setBlock(pos, state.setValue(AgCropBlock.AGE, age + 1), Block.UPDATE_ALL);
        return true;
    }

    /** Drops the product (base + Gain bonus), sometimes a seed, and sets the plant back to age 1. */
    public static int harvest(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof AgCropBlockEntity crop)) return 0;
        var s = crop.species();
        int count = s.baseYield() + random.nextInt(1 + (int) (CropStats.effective(crop.stats().gain()) / 6));
        Block.popResource(level, pos, new ItemStack(s.product().get(), count));
        if (random.nextFloat() < 0.1f + crop.stats().gain() * 0.01f)
            Block.popResource(level, pos, SeedItem.of(s, crop.stats()));
        level.setBlock(pos, state.setValue(AgCropBlock.AGE, 1), Block.UPDATE_ALL);
        return count;
    }
}
