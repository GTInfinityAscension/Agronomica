package com.gtinfinityascension.agronomica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.agronomica.registry.AgItems;

/**
 * Steam-era nitrogen: fill with plant matter (rotten flesh and biomass count double), wait for it to cure, collect
 * compost. Stages 0-7 fill, 8 = curing, 9 = ready.
 */
public class CompostBarrelBlock extends Block {

    public static final int FULL = 8, CURING = 8, READY = 9;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, READY);
    public static final TagKey<Item> COMPOSTABLE = TagKey.create(net.minecraft.core.registries.Registries.ITEM,
            new net.minecraft.resources.ResourceLocation(Agronomica.MOD_ID, "compostable"));
    public static final TagKey<Item> RICH = TagKey.create(net.minecraft.core.registries.Registries.ITEM,
            new net.minecraft.resources.ResourceLocation(Agronomica.MOD_ID, "compostable_rich"));
    /**
     * EN: 5 minutes to cure and 2 compost per barrel: slow on purpose, the MV urea line is the real nitrogen source.
     * PT: 5 minutos de cura e 2 compostos por barril: lento de propósito; a ureia do MV é a fonte de nitrogênio real.
     * ZH: 熟化 5 分钟、每桶 2 份堆肥：故意偏慢，MV 尿素线才是主要氮源。
     */
    public static final int CURE_TICKS = 6000, OUTPUT = 2;
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

    public CompostBarrelBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        int fill = state.getValue(LEVEL);
        if (fill == READY) {
            if (!level.isClientSide) {
                popResource(level, pos.above(), new ItemStack(AgItems.COMPOST.get(), OUTPUT));
                level.setBlock(pos, state.setValue(LEVEL, 0), Block.UPDATE_ALL);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        ItemStack held = player.getItemInHand(hand);
        if (fill >= FULL || !held.is(COMPOSTABLE)) return InteractionResult.PASS;
        if (!level.isClientSide) add((ServerLevel) level, pos, state, held.is(RICH) ? 2 : 1);
        if (!player.getAbilities().instabuild) held.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** Adds plant matter; when it reaches {@link #FULL} the barrel starts curing. */
    public static void add(ServerLevel level, BlockPos pos, BlockState state, int amount) {
        int fill = Math.min(FULL, state.getValue(LEVEL) + amount);
        level.setBlock(pos, state.setValue(LEVEL, fill), Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1, 1);
        if (fill == FULL) level.scheduleTick(pos, state.getBlock(), CURE_TICKS);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LEVEL) == CURING) {
            level.setBlock(pos, state.setValue(LEVEL, READY), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1, 1);
        }
    }
}
