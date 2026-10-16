package com.gtinfinityascension.agronomica;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Agronomica.MOD_ID)
@SuppressWarnings("removal")
public final class Agronomica {

    public static final String MOD_ID = "agronomica";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    /** The support every crop is planted on; two sticks side by side let neighbours cross-breed. */
    public static final RegistryObject<Item> CROP_STICKS = ITEMS.register("crop_sticks",
            () -> new Item(new Item.Properties()));

    public Agronomica() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }
}
