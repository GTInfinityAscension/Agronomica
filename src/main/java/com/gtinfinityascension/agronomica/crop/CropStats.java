package com.gtinfinityascension.agronomica.crop;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/** Growth, Gain and Resistance of one plant, each 1..31. */
public record CropStats(int growth, int gain, int resistance) {

    public static final int MAX = 31;
    public static final CropStats WILD = new CropStats(1, 1, 1);

    public CropStats {
        growth = Mth.clamp(growth, 1, MAX);
        gain = Mth.clamp(gain, 1, MAX);
        resistance = Mth.clamp(resistance, 1, MAX);
    }

    /**
     * EN: Diminishing returns above 20: each point past 20 counts half (proposal v02, like CropsNH's soft cap).
     * PT: Retorno decrescente acima de 20: cada ponto além de 20 vale metade (proposta v02, como o teto do CropsNH).
     * ZH: 20 以上收益递减：超过 20 的每点只算一半（提案 v02，类似 CropsNH 的软上限）。
     */
    public static float effective(int stat) {
        return stat <= 20 ? stat : 20 + (stat - 20) / 2f;
    }

    /** Child of two parents: the average of each stat, then -1..+2 so breeding slowly climbs. */
    public static CropStats cross(CropStats a, CropStats b, RandomSource random) {
        return new CropStats(child(a.growth, b.growth, random), child(a.gain, b.gain, random),
                child(a.resistance, b.resistance, random));
    }

    private static int child(int a, int b, RandomSource random) {
        return (a + b) / 2 + random.nextInt(4) - 1;
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putInt("growth", growth);
        tag.putInt("gain", gain);
        tag.putInt("resistance", resistance);
        return tag;
    }

    public static CropStats load(CompoundTag tag) {
        if (!tag.contains("growth")) return WILD;
        return new CropStats(tag.getInt("growth"), tag.getInt("gain"), tag.getInt("resistance"));
    }
}
