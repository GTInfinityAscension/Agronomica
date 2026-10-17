package com.gtinfinityascension.agronomica.crop;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

/** Nitrogen, phosphorus and potassium left in the soil under one crop, 0..{@link #CAP} each. */
public final class Nutrients {

    public static final int CAP = 100;
    /** Natural soil starts with a little of each, enough for a couple of cycles of a light crop. */
    public static final int NATURAL = 24;

    private int n = NATURAL, p = NATURAL, k = NATURAL;

    public int n() {
        return n;
    }

    public int p() {
        return p;
    }

    public int k() {
        return k;
    }

    public void add(int dn, int dp, int dk) {
        n = Mth.clamp(n + dn, 0, CAP);
        p = Mth.clamp(p + dp, 0, CAP);
        k = Mth.clamp(k + dk, 0, CAP);
    }

    /** Takes the demand if every part is there; otherwise takes nothing and returns false. */
    public boolean consume(int dn, int dp, int dk) {
        if (n < dn || p < dp || k < dk) return false;
        n -= dn;
        p -= dp;
        k -= dk;
        return true;
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putInt("n", n);
        tag.putInt("p", p);
        tag.putInt("k", k);
        return tag;
    }

    public void load(CompoundTag tag) {
        if (!tag.contains("n")) return;
        n = tag.getInt("n");
        p = tag.getInt("p");
        k = tag.getInt("k");
    }
}
