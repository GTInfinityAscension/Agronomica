package com.gtinfinityascension.agronomica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;

import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropSpecies;
import com.gtinfinityascension.agronomica.crop.CropStats;

import java.util.ArrayList;
import java.util.List;

/** Crossed sticks with two or more mature neighbours turn into a child plant. */
public final class CrossBreeding {

    /**
     * EN: Chance per random tick once two parents are ready; a mutation pair mutates half the time.
     * PT: Chance por random tick quando há dois pais prontos; um par com mutação muta metade das vezes.
     * ZH: 有两个成熟亲本时每次随机刻的概率；可变异的组合有一半概率变异。
     */
    public static final float CROSS_CHANCE = 0.35f, MUTATION_CHANCE = 0.5f;

    private CrossBreeding() {}

    public static boolean tryCross(ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() >= CROSS_CHANCE) return false;
        return crossNow(level, pos, random);
    }

    /** Breeds right away (used by the random tick after its roll, and by tests). */
    public static boolean crossNow(ServerLevel level, BlockPos pos, RandomSource random) {
        List<AgCropBlockEntity> parents = new ArrayList<>();
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (level.getBlockEntity(pos.relative(side)) instanceof AgCropBlockEntity crop && crop.isMature())
                parents.add(crop);
        }
        if (parents.size() < 2) return false;
        var a = parents.remove(random.nextInt(parents.size()));
        var b = parents.get(random.nextInt(parents.size()));
        CropSpecies child = CropRegistry.mutation(a.species().id(), b.species().id());
        if (child == null || random.nextFloat() >= MUTATION_CHANCE)
            child = random.nextBoolean() ? a.species() : b.species();
        CropStats stats = CropStats.cross(a.stats(), b.stats(), random);
        level.setBlock(pos, child.block().get().defaultBlockState(), Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof AgCropBlockEntity planted) planted.plant(stats);
        return true;
    }
}
