package com.gtinfinityascension.agronomica.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import com.gtinfinityascension.agronomica.Agronomica;
import com.gtinfinityascension.gtialib.test.Sweeps;

/** Sweeps over what Agronomica registers (GTIA Lib test kit). */
@PrefixGameTestTemplate(false)
@GameTestHolder(Agronomica.MOD_ID)
public final class RegistryGameTests {

    private RegistryGameTests() {}

    @GameTest(template = "qa_empty_5x5", timeoutTicks = 20)
    public static void everyNameExistsInAllThreeLanguages(GameTestHelper h) {
        Sweeps.namesInAllLanguages(Agronomica.MOD_ID).assertClean(h, 1);
        h.succeed();
    }
}
