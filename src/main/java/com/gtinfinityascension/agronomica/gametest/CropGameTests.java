package com.gtinfinityascension.agronomica.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.block.AgCropBlock;
import com.gtinfinityascension.agronomica.block.AgCropBlockEntity;
import com.gtinfinityascension.agronomica.block.CropGrowth;
import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropSpecies;
import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.crop.Nutrients;
import com.gtinfinityascension.agronomica.registry.AgItems;

/** Planting, growth on N/P/K, starvation and harvest. */
@PrefixGameTestTemplate(false)
@GameTestHolder(Agronomica.MOD_ID)
public final class CropGameTests {

    static final String TEMPLATE = "qa_empty_5x5";
    static final BlockPos CROP = new BlockPos(2, 2, 2);

    private CropGameTests() {}

    static AgCropBlockEntity plant(GameTestHelper h, BlockPos pos, CropSpecies species, CropStats stats) {
        h.setBlock(pos.below(), Blocks.FARMLAND);
        h.setBlock(pos, species.block().get());
        var crop = (AgCropBlockEntity) h.getBlockEntity(pos);
        crop.plant(stats);
        return crop;
    }

    static int age(GameTestHelper h, BlockPos pos) {
        return h.getBlockState(pos).getValue(AgCropBlock.AGE);
    }

    static boolean grow(GameTestHelper h, BlockPos pos) {
        var abs = h.absolutePos(pos);
        return CropGrowth.growNow(h.getLevel(), abs, h.getLevel().getBlockState(abs),
                (AgCropBlockEntity) h.getBlockEntity(pos));
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void cropGrowsToMaturityAndEatsTheSoil(GameTestHelper h) {
        var crop = plant(h, CROP, CropRegistry.HEMP, CropStats.WILD);
        for (int i = 0; i < AgCropBlock.MAX_AGE; i++) h.assertTrue(grow(h, CROP), "step " + i + " should grow");
        h.assertTrue(age(h, CROP) == AgCropBlock.MAX_AGE, "hemp should be mature");
        h.assertFalse(grow(h, CROP), "a mature crop does not grow further");
        int spentN = Nutrients.NATURAL - crop.nutrients().n();
        h.assertTrue(spentN > 0, "hemp should take nitrogen, took " + spentN);
        h.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void starvedCropStopsAndFertilizerRestartsIt(GameTestHelper h) {
        var crop = plant(h, CROP, CropRegistry.ELEPHANT_GRASS, CropStats.WILD);
        crop.nutrients().consume(crop.nutrients().n(), 0, 0);
        h.assertFalse(grow(h, CROP), "no nitrogen, no growth");
        h.assertTrue(age(h, CROP) == 0, "age must stay 0 when starved");
        crop.fertilize(40, 0, 0);
        h.assertTrue(grow(h, CROP), "fertilised crop grows again");
        h.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void harvestDropsProductAndKeepsThePlant(GameTestHelper h) {
        plant(h, CROP, CropRegistry.RAPESEED, new CropStats(1, 31, 1));
        for (int i = 0; i < AgCropBlock.MAX_AGE; i++) grow(h, CROP);
        var abs = h.absolutePos(CROP);
        int count = CropGrowth.harvest(h.getLevel(), abs, h.getLevel().getBlockState(abs), h.getLevel().random);
        h.assertTrue(count >= CropRegistry.RAPESEED.baseYield(), "harvest gave " + count);
        h.assertTrue(age(h, CROP) == 1, "harvest sets the plant back to age 1");
        h.assertTrue(
                h.getLevel().getEntitiesOfClass(ItemEntity.class, new net.minecraft.world.phys.AABB(abs).inflate(2))
                        .stream().anyMatch(e -> e.getItem().is(AgItems.OILY_SEED.get())),
                "oily seeds should drop");
        h.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void resistanceCutsDemandAndStatsSoftCap(GameTestHelper h) {
        h.assertTrue(CropGrowth.scaledDemand(4, new CropStats(1, 1, 31)) < CropGrowth.scaledDemand(4, CropStats.WILD),
                "Resistance 31 should need less N/P/K");
        h.assertTrue(CropStats.effective(31) == 25.5f, "31 counts as 25.5 after the soft cap");
        h.assertTrue(CropGrowth.growthChance(new CropStats(31, 1, 1)) < 1f, "growth chance stays below 100 %");
        h.assertTrue(new CropStats(99, -5, 40).equals(new CropStats(31, 1, 31)), "stats clamp to 1..31");
        h.succeed();
    }
}
