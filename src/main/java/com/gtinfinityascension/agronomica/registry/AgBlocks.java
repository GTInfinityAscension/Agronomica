package com.gtinfinityascension.agronomica.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.block.AgCropBlock;
import com.gtinfinityascension.agronomica.block.CompostBarrelBlock;
import com.gtinfinityascension.agronomica.block.CropSticksBlock;

public final class AgBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Agronomica.MOD_ID);

    public static final RegistryObject<Block> CROP_STICKS = BLOCKS.register("crop_sticks",
            () -> new CropSticksBlock(props()));
    public static final RegistryObject<Block> COMPOST_BARREL = BLOCKS.register("compost_barrel",
            () -> new CompostBarrelBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HEMP = crop("hemp");
    public static final RegistryObject<Block> RAPESEED = crop("rapeseed");
    public static final RegistryObject<Block> RUBBER_BUSH = crop("rubber_bush");
    public static final RegistryObject<Block> ELEPHANT_GRASS = crop("elephant_grass");

    private AgBlocks() {}

    private static RegistryObject<Block> crop(String id) {
        return BLOCKS.register(id, () -> new AgCropBlock(props().randomTicks(), id));
    }

    private static BlockBehaviour.Properties props() {
        return BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.CROP).noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }
}
