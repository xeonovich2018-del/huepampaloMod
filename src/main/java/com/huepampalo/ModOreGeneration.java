package com.huepampalo;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModOreGeneration {

    public static final ResourceKey<PlacedFeature> VENOM_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            HuepampaloMod.id("venom_placed"));

    public static void generateOres() {

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                VENOM_PLACED_KEY);
    }
}