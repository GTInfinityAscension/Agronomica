# Agronomica

Industrial crop breeding for GregTech modpacks on Minecraft 1.20.1 (Forge), made for [GregTech Infinity Ascension](https://github.com/GTInfinityAscension/GregTech-Infinity-Ascension).

> **Status: early development.** Nothing here is playable yet. Requires GTCEu and [GTIA Lib](https://github.com/GTInfinityAscension/GTIA-Lib).

## What it will be

- **Crop sticks, stats and cross-breeding.** Growth, Gain and Resistance, with diminishing returns at the top.
- **NPK soil.** Crops use up nitrogen, phosphorus and potassium; fertiliser comes from real chemistry lines.
- **Climate.** Each crop has a temperature and humidity range; greenhouses control climate and CO₂.
- **Metal crops that feed ore processing.** A leaf plus crushed ore and a reagent gives more purified ore; the direct leaf-to-dust route exists but is worse.
- **Phytomining.** Hyperaccumulator plants recover metal from contaminated soil, such as mining tailings.
- **Machines for scale.** Large farms run as multiblocks with the crop stored as an item, so they stay light on the server.

Inspired by GTNH's CropsNH and by Forestry, with its own mechanics.

## Building

```sh
./gradlew build
./gradlew runGameTestServer
```

Every name ships in en_us, pt_br and zh_cn. See the organisation's [contribution guidelines](https://github.com/GTInfinityAscension/.github/blob/main/CONTRIBUTING.md) and [AI policy](https://github.com/GTInfinityAscension/.github/blob/main/AI_POLICY.md).

## License

[LGPL-3.0](LICENSE.MD).
