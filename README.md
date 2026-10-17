# Agronomica

Industrial crop breeding for GregTech modpacks on Minecraft 1.20.1 (Forge), made for [GregTech Infinity Ascension](https://github.com/GTInfinityAscension/GregTech-Infinity-Ascension).

> **Status: early development.** The core works: crop sticks, four Steam/LV plants (hemp, rapeseed, elephant grass, rubber bush), Growth/Gain/Resistance, cross-breeding with mutations, NPK soil and fertilisers. Hemp and rapeseed seeds drop from grass. Requires GTCEu and [GTIA Lib](https://github.com/GTInfinityAscension/GTIA-Lib).

## How to play (current core)

1. Till farmland, place **Crop Sticks** on it and right-click with a seed.
2. Plants grow on random ticks with light 9+, taking N/P/K from the soil. Right-click with a fertiliser to refill it.
3. Right-click a mature plant to harvest; it goes back to its first leaves.
4. Right-click empty sticks with another stick to **cross** them. Crossed sticks between two mature plants grow a child with averaged stats, sometimes a new species (hemp + rapeseed → elephant grass; rapeseed + elephant grass → rubber bush).

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
