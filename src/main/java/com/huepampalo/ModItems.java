package com.huepampalo;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems {

    public static Item VENOM_PICKAXE;

    public static void register() {

        VENOM_PICKAXE = Registry.register(
                BuiltInRegistries.ITEM,
                HuepampaloMod.id("venom_pickaxe"),
                new VenomPickaxe());

        System.out.println("Items registered!");
    }
}