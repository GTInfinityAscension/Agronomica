package com.gtinfinityascension.agronomica.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.block.AgCropBlockEntity;
import com.gtinfinityascension.agronomica.block.CropSticksBlock;
import com.gtinfinityascension.agronomica.block.CrossBreeding;
import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.registry.AgBlocks;

import java.util.Set;

import static com.gtinfinityascension.agronomica.gametest.CropGameTests.*;

/** Crossed sticks between mature parents breed a child; without two parents nothing happens. */
@PrefixGameTestTemplate(false)
@GameTestHolder(Agronomica.MOD_ID)
public final class BreedingGameTests {

    private BreedingGameTests() {}

    private static void crossedSticks(GameTestHelper h, BlockPos pos) {
        h.setBlock(pos.below(), Blocks.FARMLAND);
        h.setBlock(pos, AgBlocks.CROP_STICKS.get().defaultBlockState().setValue(CropSticksBlock.CROSSED, true));
    }

    private static void mature(GameTestHelper h, BlockPos pos) {
        while (grow(h, pos)) {}
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void twoMatureParentsBreedAChild(GameTestHelper h) {
        plant(h, CROP.west(), CropRegistry.HEMP, new CropStats(10, 10, 10));
        plant(h, CROP.east(), CropRegistry.RAPESEED, new CropStats(20, 20, 20));
        mature(h, CROP.west());
        mature(h, CROP.east());
        crossedSticks(h, CROP);
        h.assertTrue(CrossBreeding.crossNow(h.getLevel(), h.absolutePos(CROP), RandomSource.create(7)),
                "crossing should succeed");
        var child = (AgCropBlockEntity) h.getBlockEntity(CROP);
        h.assertTrue(Set.of("hemp", "rapeseed", "elephant_grass").contains(child.species().id()),
                "unexpected child " + child.species().id());
        int g = child.stats().growth();
        h.assertTrue(g >= 14 && g <= 17, "child Growth should be the parents' average -1..+2, was " + g);
        h.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void oneParentIsNotEnough(GameTestHelper h) {
        plant(h, CROP.west(), CropRegistry.HEMP, CropStats.WILD);
        mature(h, CROP.west());
        plant(h, CROP.east(), CropRegistry.HEMP, CropStats.WILD);
        crossedSticks(h, CROP);
        h.assertFalse(CrossBreeding.crossNow(h.getLevel(), h.absolutePos(CROP), RandomSource.create(1)),
                "an immature neighbour must not count as a parent");
        h.assertBlockPresent(AgBlocks.CROP_STICKS.get(), CROP);
        h.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 20)
    public static void mutationPairsAreDefined(GameTestHelper h) {
        h.assertTrue(CropRegistry.mutation("rapeseed", "hemp") == CropRegistry.ELEPHANT_GRASS,
                "hemp + rapeseed → elephant grass");
        h.assertTrue(CropRegistry.mutation("elephant_grass", "rapeseed") == CropRegistry.RUBBER_BUSH,
                "rapeseed + elephant grass → rubber bush");
        h.assertTrue(CropRegistry.mutation("hemp", "hemp") == null, "same species has no mutation");
        h.succeed();
    }
}
