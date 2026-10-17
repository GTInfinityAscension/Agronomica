package com.gtinfinityascension.agronomica.crop;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * One plant kind. {@code demand} is the N/P/K a growth step takes from the soil; {@code baseYield} is what a
 * harvest gives before Gain.
 */
public record CropSpecies(String id, int color, Supplier<Item> product, int baseYield, int demandN, int demandP,
                          int demandK, Supplier<Block> block) {}
