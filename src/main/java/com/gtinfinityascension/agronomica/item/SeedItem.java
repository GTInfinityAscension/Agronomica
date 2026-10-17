package com.gtinfinityascension.agronomica.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.gtinfinityascension.agronomica.block.AgCropBlockEntity;
import com.gtinfinityascension.agronomica.block.CropSticksBlock;
import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropSpecies;
import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.registry.AgBlocks;
import com.gtinfinityascension.agronomica.registry.AgItems;
import com.gtinfinityascension.gtialib.lang.Tr;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** One seed item for every species; the species and its stats live in NBT. */
public class SeedItem extends Item {

    static {
        Tr.of("agronomica.seed.stats", "Growth %s · Gain %s · Resistance %s",
                "Crescimento %s · Ganho %s · Resistência %s", "生长 %s · 产量 %s · 抗性 %s");
        Tr.of("agronomica.seed.unknown", "Unknown seeds", "Sementes desconhecidas", "未知种子");
        Tr.of("agronomica.seed.unanalyzed", "Unanalyzed: use a Seed Analyzer to see its stats",
                "Não analisada: use um Analisador de Sementes para ver os atributos", "未分析：使用种子分析仪查看属性");
    }

    public SeedItem(Properties properties) {
        super(properties);
    }

    public static ItemStack of(CropSpecies species, CropStats stats) {
        var stack = new ItemStack(AgItems.SEEDS.get());
        var tag = stack.getOrCreateTag();
        tag.putString("species", species.id());
        tag.put("stats", stats.save(new CompoundTag()));
        return stack;
    }

    public static @Nullable CropSpecies species(ItemStack stack) {
        var tag = stack.getTag();
        return tag == null ? null : CropRegistry.get(tag.getString("species"));
    }

    /** Set by the Seed Analyzer (GTIACore); until then the stats stay hidden. */
    public static boolean isAnalyzed(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().getBoolean("analyzed");
    }

    public static CropStats stats(ItemStack stack) {
        var tag = stack.getTag();
        return tag == null ? CropStats.WILD : CropStats.load(tag.getCompound("stats"));
    }

    @Override
    public Component getName(ItemStack stack) {
        var species = species(stack);
        if (species == null) return Component.translatable("agronomica.seed.unknown");
        return Component.translatable("item.agronomica.seeds.named",
                Component.translatable("agronomica.species." + species.id()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        if (species(stack) == null) return;
        if (!isAnalyzed(stack)) {
            lines.add(Component.translatable("agronomica.seed.unanalyzed").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }
        var s = stats(stack);
        lines.add(Component.translatable("agronomica.seed.stats", s.growth(), s.gain(), s.resistance())
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var state = level.getBlockState(pos);
        var species = species(context.getItemInHand());
        if (species == null || !state.is(AgBlocks.CROP_STICKS.get()) || state.getValue(CropSticksBlock.CROSSED))
            return InteractionResult.PASS;
        if (!level.isClientSide) {
            level.setBlock(pos, species.block().get().defaultBlockState(), Block.UPDATE_ALL);
            if (level.getBlockEntity(pos) instanceof AgCropBlockEntity crop)
                crop.plant(stats(context.getItemInHand()));
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild)
                context.getItemInHand().shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
