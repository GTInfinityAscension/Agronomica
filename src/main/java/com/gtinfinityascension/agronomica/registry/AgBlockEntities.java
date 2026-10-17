package com.gtinfinityascension.agronomica.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.block.AgCropBlockEntity;

public final class AgBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> TYPES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITY_TYPES, Agronomica.MOD_ID);

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<AgCropBlockEntity>> CROP = TYPES.register("crop",
            () -> BlockEntityType.Builder.of(AgCropBlockEntity::new, AgBlocks.HEMP.get(), AgBlocks.RAPESEED.get(),
                    AgBlocks.RUBBER_BUSH.get(), AgBlocks.ELEPHANT_GRASS.get()).build(null));

    private AgBlockEntities() {}
}
