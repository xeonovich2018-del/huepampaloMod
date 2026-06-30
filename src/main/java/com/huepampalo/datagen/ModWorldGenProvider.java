package com.huepampalo.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

import com.huepampalo.ModConfiguredFeatures;
import com.huepampalo.ModPlacedFeatures;

public class ModWorldGenProvider extends FabricDynamicRegistryProvider {

    public ModWorldGenProvider(FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {

        // ❌ НЕ addAll (это ванила)
        // ✔ ТОЛЬКО твои фичи:

        // entries.add(
        // ModConfiguredFeatures.VENOM_ORE_KEY,
        // ModConfiguredFeatures.VENOM_ORE);

        // entries.add(
        // ModPlacedFeatures.VENOM_ORE_PLACED_KEY,
        // ModPlacedFeatures.VENOM_ORE_PLACED);
    }

    @Override
    public String getName() {
        return "huepampalo-worldgen";
    }
}