package com.gtinfinityascension.agronomica;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gtinfinityascension.agronomica.client.AgClient;
import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.item.SeedItem;
import com.gtinfinityascension.agronomica.loot.WildSeedModifier;
import com.gtinfinityascension.agronomica.registry.AgBlockEntities;
import com.gtinfinityascension.agronomica.registry.AgBlocks;
import com.gtinfinityascension.agronomica.registry.AgItems;
import com.gtinfinityascension.gtialib.lang.Tr;
import com.gtinfinityascension.gtialib.lang.TrLanguageProvider;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Agronomica.MOD_ID)
@SuppressWarnings("removal")
public final class Agronomica {

    public static final String MOD_ID = "agronomica";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister
            .create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    static {
        LOOT_MODIFIERS.register("wild_seed", () -> WildSeedModifier.CODEC);
        Tr.of("itemGroup.agronomica", "Agronomica", "Agronomica", "Agronomica");
    }

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.agronomica"))
            .icon(() -> new ItemStack(AgItems.CROP_STICKS.get()))
            .displayItems((params, out) -> {
                AgItems.ITEMS.getEntries().forEach(item -> {
                    if (item != AgItems.SEEDS) out.accept(item.get());
                });
                CropRegistry.all().forEach(s -> out.accept(SeedItem.of(s, CropStats.WILD)));
            }).build());

    public Agronomica() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        AgBlocks.BLOCKS.register(bus);
        AgItems.ITEMS.register(bus);
        AgBlockEntities.TYPES.register(bus);
        LOOT_MODIFIERS.register(bus);
        TABS.register(bus);
        CropRegistry.all();
        bus.addListener(Agronomica::gatherData);
        if (FMLEnvironment.dist == Dist.CLIENT) bus.addListener(AgClient::itemColors);
    }

    private static void gatherData(GatherDataEvent event) {
        TrLanguageProvider.addAll(event, MOD_ID, true, "item." + MOD_ID + ".", "block." + MOD_ID + ".", MOD_ID + ".",
                "itemGroup." + MOD_ID);
    }
}
