package com.huepampalo;

import com.huepampalo.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> VENOM_ORE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            HuepampaloMod.id("venom_ore"));

    public static void configure(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        List<OreConfiguration.TargetBlockState> targets = List.of(
                OreConfiguration.target(
                        new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                        ModBlocks.VENOM_BLOCK.defaultBlockState()));

        FeatureUtils.register(
                context,
                VENOM_ORE,
                Feature.ORE,
                new OreConfiguration(targets, 9));
    }
}