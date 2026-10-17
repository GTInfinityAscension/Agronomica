package com.gtinfinityascension.agronomica.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.block.CompostBarrelBlock;
import com.gtinfinityascension.agronomica.registry.AgBlocks;
import com.gtinfinityascension.agronomica.registry.AgItems;

/** Compost Barrel: fill, cure, collect; nothing comes out before it cures. */
@PrefixGameTestTemplate(false)
@GameTestHolder(Agronomica.MOD_ID)
public final class CompostGameTests {

    private static final BlockPos BARREL = new BlockPos(2, 2, 2);

    private CompostGameTests() {}

    private static int level(GameTestHelper h) {
        return h.getBlockState(BARREL).getValue(CompostBarrelBlock.LEVEL);
    }

    @GameTest(template = CropGameTests.TEMPLATE, timeoutTicks = 20)
    public static void fullBarrelCuresIntoCompost(GameTestHelper h) {
        h.setBlock(BARREL, AgBlocks.COMPOST_BARREL.get());
        var abs = h.absolutePos(BARREL);
        for (int i = 0; i < 4; i++) CompostBarrelBlock.add(h.getLevel(), abs, h.getLevel().getBlockState(abs), 2);
        h.assertTrue(level(h) == CompostBarrelBlock.CURING, "four rich items fill the barrel, level " + level(h));
        h.getBlockState(BARREL).tick(h.getLevel(), abs, h.getLevel().random);
        h.assertTrue(level(h) == CompostBarrelBlock.READY, "the scheduled tick finishes curing");
        var player = h.makeMockPlayer();
        h.getBlockState(BARREL).use(h.getLevel(), player, net.minecraft.world.InteractionHand.MAIN_HAND,
                new net.minecraft.world.phys.BlockHitResult(abs.getCenter(), net.minecraft.core.Direction.UP, abs,
                        false));
        int compost = h.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(abs).inflate(3)).stream()
                .filter(e -> e.getItem().is(AgItems.COMPOST.get())).mapToInt(e -> e.getItem().getCount()).sum();
        h.assertTrue(compost == CompostBarrelBlock.OUTPUT, "expected 2 compost, got " + compost);
        h.assertTrue(level(h) == 0, "the barrel empties after collecting");
        h.succeed();
    }

    @GameTest(template = CropGameTests.TEMPLATE, timeoutTicks = 20)
    public static void halfBarrelDoesNotCure(GameTestHelper h) {
        h.setBlock(BARREL, AgBlocks.COMPOST_BARREL.get());
        var abs = h.absolutePos(BARREL);
        CompostBarrelBlock.add(h.getLevel(), abs, h.getLevel().getBlockState(abs), 1);
        h.getBlockState(BARREL).tick(h.getLevel(), abs, h.getLevel().random);
        h.assertTrue(level(h) == 1, "a barrel that is not full never cures, level " + level(h));
        h.succeed();
    }
}
