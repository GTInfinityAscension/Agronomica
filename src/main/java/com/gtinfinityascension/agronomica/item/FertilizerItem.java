package com.gtinfinityascension.agronomica.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import com.gtinfinityascension.agronomica.block.AgCropBlockEntity;
import com.gtinfinityascension.gtialib.lang.Tr;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Adds N/P/K to the soil under a planted crop. */
public class FertilizerItem extends Item {

    static {
        Tr.of("agronomica.fertilizer.adds", "Adds N %s · P %s · K %s to a crop's soil",
                "Adiciona N %s · P %s · K %s ao solo da planta", "为作物土壤添加 N %s · P %s · K %s");
    }

    private final int n, p, k;

    public FertilizerItem(Properties properties, int n, int p, int k) {
        super(properties);
        this.n = n;
        this.p = p;
        this.k = k;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("agronomica.fertilizer.adds", n, p, k).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        if (!(level.getBlockEntity(context.getClickedPos()) instanceof AgCropBlockEntity crop))
            return InteractionResult.PASS;
        if (!level.isClientSide) {
            crop.fertilize(n, p, k);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild)
                context.getItemInHand().shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
