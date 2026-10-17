package com.gtinfinityascension.agronomica.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.item.SeedItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/** Grass sometimes drops a wild seed (stats 1/1/1) of a species that grows in the wild. */
public class WildSeedModifier extends LootModifier {

    public static final Codec<WildSeedModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(Codec.STRING.fieldOf("species").forGetter(m -> m.species))
            .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
            .apply(inst, WildSeedModifier::new));

    private final String species;
    private final float chance;

    public WildSeedModifier(LootItemCondition[] conditions, String species, float chance) {
        super(conditions);
        this.species = species;
        this.chance = chance;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        var s = CropRegistry.get(species);
        if (s != null && context.getRandom().nextFloat() < chance) loot.add(SeedItem.of(s, CropStats.WILD));
        return loot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
