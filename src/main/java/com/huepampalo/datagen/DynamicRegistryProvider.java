package com.huepampalo.datagen;

import com.huepampalo.HuepampaloMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class DynamicRegistryProvider extends FabricDynamicRegistryProvider {

    public DynamicRegistryProvider(
            FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {

        // configured features
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));

        // placed features
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return HuepampaloMod.MOD_ID + "_worldgen";
    }
}