package com.gtinfinityascension.agronomica.client;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import com.gtinfinityascension.agronomica.item.SeedItem;
import com.gtinfinityascension.agronomica.registry.AgItems;

/** Client hooks: seeds are tinted with their species colour. */
public final class AgClient {

    private AgClient() {}

    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, layer) -> {
            var species = SeedItem.species(stack);
            return layer == 0 && species != null ? species.color() : 0xFFFFFF;
        }, AgItems.SEEDS.get());
    }
}
