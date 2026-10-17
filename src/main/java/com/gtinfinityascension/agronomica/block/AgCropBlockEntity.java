package com.gtinfinityascension.agronomica.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.gtinfinityascension.agronomica.crop.CropSpecies;
import com.gtinfinityascension.agronomica.crop.CropStats;
import com.gtinfinityascension.agronomica.crop.Nutrients;
import com.gtinfinityascension.agronomica.registry.AgBlockEntities;

/** Stats of the plant and the N/P/K left in the soil under it. */
public class AgCropBlockEntity extends BlockEntity {

    private CropStats stats = CropStats.WILD;
    private final Nutrients nutrients = new Nutrients();

    public AgCropBlockEntity(BlockPos pos, BlockState state) {
        super(AgBlockEntities.CROP.get(), pos, state);
    }

    public CropSpecies species() {
        return ((AgCropBlock) getBlockState().getBlock()).species();
    }

    public CropStats stats() {
        return stats;
    }

    public Nutrients nutrients() {
        return nutrients;
    }

    public boolean isMature() {
        return getBlockState().getValue(AgCropBlock.AGE) == AgCropBlock.MAX_AGE;
    }

    public void plant(CropStats stats) {
        this.stats = stats;
        setChanged();
    }

    public void fertilize(int n, int p, int k) {
        nutrients.add(n, p, k);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("stats", stats.save(new CompoundTag()));
        tag.put("soil", nutrients.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        stats = CropStats.load(tag.getCompound("stats"));
        nutrients.load(tag.getCompound("soil"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
