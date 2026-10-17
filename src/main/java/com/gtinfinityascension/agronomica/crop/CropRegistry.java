package com.gtinfinityascension.agronomica.crop;

import com.gtinfinityascension.agronomica.registry.AgBlocks;
import com.gtinfinityascension.agronomica.registry.AgItems;
import com.gtinfinityascension.gtialib.lang.Tr;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/** The Steam/LV plants (proposal v02: biomass, fibre, oil and resin) and how they cross. */
public final class CropRegistry {

    private static final Map<String, CropSpecies> SPECIES = new LinkedHashMap<>();
    private static final Map<String, String> MUTATIONS = new LinkedHashMap<>();

    // EN: demand is N/P/K per growth step; fibre wants N, oil wants P, resin wants K, biomass eats a lot of N.
    // PT: demanda é N/P/K por estágio; fibra pede N, óleo pede P, resina pede K, biomassa come muito N.
    // ZH: 需求为每个生长阶段的 N/P/K；纤维偏 N，油料偏 P，树脂偏 K，生物质大量消耗 N。
    public static final CropSpecies HEMP = add(new CropSpecies("hemp", 0x4F8F6F, AgItems.HEMP_FIBER, 2, 3, 1, 1,
            AgBlocks.HEMP));
    public static final CropSpecies RAPESEED = add(new CropSpecies("rapeseed", 0xE8D23A, AgItems.OILY_SEED, 2, 1, 3,
            1, AgBlocks.RAPESEED));
    public static final CropSpecies ELEPHANT_GRASS = add(new CropSpecies("elephant_grass", 0x7FA33A,
            AgItems.BIOMASS_BUNDLE, 3, 4, 1, 2, AgBlocks.ELEPHANT_GRASS));
    public static final CropSpecies RUBBER_BUSH = add(new CropSpecies("rubber_bush", 0x5B6B3A, AgItems.LATEX, 1, 1, 1,
            3, AgBlocks.RUBBER_BUSH));

    static {
        // EN: Only hemp and rapeseed grow wild; the other two come from crossing.
        // PT: Só cânhamo e colza nascem no mato; as outras duas vêm de cruzamento.
        // ZH: 只有大麻和油菜可在野外获得；另外两种需杂交获得。
        mutation(HEMP, RAPESEED, ELEPHANT_GRASS);
        mutation(RAPESEED, ELEPHANT_GRASS, RUBBER_BUSH);
        Tr.of("agronomica.species.hemp", "Hemp", "Cânhamo", "大麻");
        Tr.of("agronomica.species.rapeseed", "Rapeseed", "Colza", "油菜");
        Tr.of("agronomica.species.elephant_grass", "Elephant Grass", "Capim-elefante", "象草");
        Tr.of("agronomica.species.rubber_bush", "Rubber Bush", "Arbusto de Borracha", "橡胶灌木");
        Tr.of("block.agronomica.hemp", "Hemp", "Cânhamo", "大麻");
        Tr.of("block.agronomica.rapeseed", "Rapeseed", "Colza", "油菜");
        Tr.of("block.agronomica.elephant_grass", "Elephant Grass", "Capim-elefante", "象草");
        Tr.of("block.agronomica.rubber_bush", "Rubber Bush", "Arbusto de Borracha", "橡胶灌木");
    }

    private CropRegistry() {}

    private static CropSpecies add(CropSpecies species) {
        SPECIES.put(species.id(), species);
        return species;
    }

    private static void mutation(CropSpecies a, CropSpecies b, CropSpecies child) {
        MUTATIONS.put(key(a.id(), b.id()), child.id());
    }

    private static String key(String a, String b) {
        return a.compareTo(b) < 0 ? a + "+" + b : b + "+" + a;
    }

    public static CropSpecies get(String id) {
        return SPECIES.get(id);
    }

    public static Collection<CropSpecies> all() {
        return SPECIES.values();
    }

    /** The species a cross of a and b mutates into, or null when the pair has no mutation. */
    public static CropSpecies mutation(String a, String b) {
        String child = MUTATIONS.get(key(a, b));
        return child == null ? null : SPECIES.get(child);
    }
}
