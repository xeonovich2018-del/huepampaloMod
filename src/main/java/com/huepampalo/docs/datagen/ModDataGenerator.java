package com.huepampalo.docs.datagen;

import com.huepampalo.HuepampaloMod;
import com.huepampalo.ModConfiguredFeatures;
import com.huepampalo.ModPlacedFeatures;
import com.huepampalo.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static net.minecraft.data.worldgen.placement.PlacementUtils.register;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(
                Registries.CONFIGURED_FEATURE,
                ModConfiguredFeatures::configure);

        registryBuilder.add(
                Registries.PLACED_FEATURE,
                ModPlacedFeatures::configure);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger("huepampalo-data");

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModWorldGenProvider::new);
    }

    private static class ModWorldGenProvider extends FabricDynamicRegistryProvider {

        public ModWorldGenProvider(FabricDataOutput output,
                CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {

            // ❗ ВАЖНО: принудительно триггерим bootstrap lookup
            registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
            registries.lookupOrThrow(Registries.PLACED_FEATURE);

            // теперь экспорт
            entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
            entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
        }

        @Override
        public String getName() {
            return "Huepampalo WorldGen";
        }
    }
}