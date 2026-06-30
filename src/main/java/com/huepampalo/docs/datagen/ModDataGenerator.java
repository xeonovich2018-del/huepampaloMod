package com.huepampalo.docs.datagen;

import com.huepampalo.ModConfiguredFeatures;
import com.huepampalo.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import java.util.concurrent.CompletableFuture;

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