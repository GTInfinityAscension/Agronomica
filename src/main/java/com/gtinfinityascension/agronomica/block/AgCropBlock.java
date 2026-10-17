package com.gtinfinityascension.agronomica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.gtinfinityascension.agronomica.crop.CropRegistry;
import com.gtinfinityascension.agronomica.crop.CropSpecies;
import com.gtinfinityascension.agronomica.item.SeedItem;
import com.gtinfinityascension.agronomica.registry.AgItems;

/** A planted crop on its sticks. Grows by random tick, eating N/P/K; right-click when mature to harvest. */
public class AgCropBlock extends Block implements EntityBlock {

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 15, 14);
    private final String speciesId;

    public AgCropBlock(Properties properties, String speciesId) {
        super(properties);
        this.speciesId = speciesId;
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    public CropSpecies species() {
        return CropRegistry.get(speciesId);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AgCropBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return CropSticksBlock.onFarmland(level, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() :
                state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        CropGrowth.tick(level, pos, state, random);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (state.getValue(AGE) != MAX_AGE) return InteractionResult.PASS;
        if (!level.isClientSide) CropGrowth.harvest((ServerLevel) level, pos, state, level.random);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        // EN: Breaking gives back the sticks and the seed with its stats (not on harvest, which keeps the block).
        // PT: Quebrar devolve os suportes e a semente com os atributos (a colheita mantém o bloco e não passa aqui).
        // ZH: 破坏时返还作物架和保留属性的种子（收获不会移除方块，不经过此处）。
        if (!state.is(newState.getBlock()) && !level.isClientSide &&
                level.getBlockEntity(pos) instanceof AgCropBlockEntity crop) {
            popResource(level, pos, new ItemStack(AgItems.CROP_STICKS.get()));
            popResource(level, pos, SeedItem.of(species(), crop.stats()));
        }
        super.onRemove(state, level, pos, newState, moving);
    }
}
