package com.gtinfinityascension.agronomica.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gtinfinityascension.agronomica.Agronomica;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Every name Agronomica registers exists in en_us, pt_br and zh_cn. */
@PrefixGameTestTemplate(false)
@GameTestHolder(Agronomica.MOD_ID)
public final class RegistryGameTests {

    private static final String[] LANGS = { "en_us", "pt_br", "zh_cn" };

    private RegistryGameTests() {}

    @GameTest(template = "qa_empty_5x5", timeoutTicks = 20)
    public static void everyNameExistsInAllThreeLanguages(GameTestHelper h) throws Exception {
        List<String> missing = new ArrayList<>();
        for (String code : LANGS) {
            JsonObject lang;
            try (InputStream in = RegistryGameTests.class
                    .getResourceAsStream("/assets/agronomica/lang/" + code + ".json")) {
                h.assertTrue(in != null, "missing lang file " + code);
                lang = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            }
            ForgeRegistries.ITEMS.getEntries().forEach(e -> {
                if (Agronomica.MOD_ID.equals(e.getKey().location().getNamespace()) &&
                        !lang.has(e.getValue().getDescriptionId()))
                    missing.add(code + ":" + e.getValue().getDescriptionId());
            });
        }
        h.assertTrue(missing.isEmpty(), "missing translations: " + missing);
        h.succeed();
    }
}
