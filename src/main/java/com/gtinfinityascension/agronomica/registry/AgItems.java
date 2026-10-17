package com.gtinfinityascension.agronomica.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.item.FertilizerItem;
import com.gtinfinityascension.agronomica.item.SeedItem;
import com.gtinfinityascension.gtialib.lang.Tr;

public final class AgItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            Agronomica.MOD_ID);

    /** The support every crop is planted on; a second stick on top crosses it so neighbours can breed. */
    public static final RegistryObject<Item> CROP_STICKS = ITEMS.register("crop_sticks",
            () -> new BlockItem(AgBlocks.CROP_STICKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> COMPOST_BARREL = ITEMS.register("compost_barrel",
            () -> new BlockItem(AgBlocks.COMPOST_BARREL.get(), new Item.Properties()));
    public static final RegistryObject<Item> SEEDS = ITEMS.register("seeds", () -> new SeedItem(new Item.Properties()));

    public static final RegistryObject<Item> HEMP_FIBER = simple("hemp_fiber");
    public static final RegistryObject<Item> OILY_SEED = simple("oily_seed");
    public static final RegistryObject<Item> LATEX = simple("latex");
    public static final RegistryObject<Item> BIOMASS_BUNDLE = simple("biomass_bundle");

    // EN: Fertiliser N/P/K per use; NPK is weaker per element but covers all three.
    // PT: Fertilizante N/P/K por uso; o NPK rende menos por elemento, mas cobre os três.
    // ZH: 每次使用的 N/P/K 肥料量；NPK 单项较少，但三项兼顾。
    public static final RegistryObject<Item> COMPOST = ITEMS.register("compost",
            () -> new FertilizerItem(new Item.Properties(), 20, 5, 5));
    public static final RegistryObject<Item> FERTILIZER_N = ITEMS.register("fertilizer_n",
            () -> new FertilizerItem(new Item.Properties(), 40, 0, 0));
    public static final RegistryObject<Item> FERTILIZER_P = ITEMS.register("fertilizer_p",
            () -> new FertilizerItem(new Item.Properties(), 0, 40, 0));
    public static final RegistryObject<Item> FERTILIZER_K = ITEMS.register("fertilizer_k",
            () -> new FertilizerItem(new Item.Properties(), 0, 0, 40));
    public static final RegistryObject<Item> FERTILIZER_NPK = ITEMS.register("fertilizer_npk",
            () -> new FertilizerItem(new Item.Properties(), 25, 25, 25));

    static {
        Tr.of("item.agronomica.crop_sticks", "Crop Sticks", "Suportes de Cultivo", "作物架");
        Tr.of("block.agronomica.crop_sticks", "Crop Sticks", "Suportes de Cultivo", "作物架");
        Tr.of("item.agronomica.seeds", "Seeds", "Sementes", "种子");
        Tr.of("block.agronomica.compost_barrel", "Compost Barrel", "Barril de Composto", "堆肥桶");
        Tr.of("item.agronomica.compost_barrel", "Compost Barrel", "Barril de Composto", "堆肥桶");
        Tr.of("item.agronomica.compost", "Compost", "Composto", "堆肥");
        Tr.of("item.agronomica.seeds.named", "%s Seeds", "Sementes de %s", "%s种子");
        Tr.of("item.agronomica.hemp_fiber", "Hemp Fiber", "Fibra de Cânhamo", "大麻纤维");
        Tr.of("item.agronomica.oily_seed", "Oily Seed", "Semente Oleosa", "油籽");
        Tr.of("item.agronomica.latex", "Latex", "Látex", "胶乳");
        Tr.of("item.agronomica.biomass_bundle", "Biomass Bundle", "Feixe de Biomassa", "生物质捆");
        Tr.of("item.agronomica.fertilizer_n", "Nitrogen Fertilizer", "Fertilizante Nitrogenado", "氮肥");
        Tr.of("item.agronomica.fertilizer_p", "Phosphate Fertilizer", "Fertilizante Fosfatado", "磷肥");
        Tr.of("item.agronomica.fertilizer_k", "Potash Fertilizer", "Fertilizante Potássico", "钾肥");
        Tr.of("item.agronomica.fertilizer_npk", "NPK Fertilizer", "Fertilizante NPK", "复合肥（NPK）");
    }

    private AgItems() {}

    private static RegistryObject<Item> simple(String id) {
        return ITEMS.register(id, () -> new Item(new Item.Properties()));
    }
}
