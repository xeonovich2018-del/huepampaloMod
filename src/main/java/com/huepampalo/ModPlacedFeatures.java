package com.huepampalo;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

        public static final ResourceKey<PlacedFeature> VENOM_ORE = ResourceKey.create(
                        Registries.PLACED_FEATURE,
                        HuepampaloMod.id("venom_ore"));

        public static void configure(BootstrapContext<PlacedFeature> context) {

                HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

                Holder<ConfiguredFeature<?, ?>> venom = configured.getOrThrow(ModConfiguredFeatures.VENOM_ORE);

                PlacementUtils.register(
                                context,
                                VENOM_ORE,
                                venom,
                                CountPlacement.of(20),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                                VerticalAnchor.absolute(-64),
                                                VerticalAnchor.absolute(64)),
                                BiomeFilter.biome());
        }
}