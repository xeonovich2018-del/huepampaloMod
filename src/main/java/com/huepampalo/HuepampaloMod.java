package com.huepampalo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huepampalo.blocks.ModBlocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.GenerationStep;

public class HuepampaloMod implements ModInitializer {
	public static final String MOD_ID = "huepampalo-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Item HUEPAMPALO_ITEM = new HuepampaloItem(
			new Item.Properties().stacksTo(1));

	private void registerVenomOre() {

		// Registry.register(Registries.CONFIGURED_FEATURE, configuredKey,
		// new ConfiguredFeature<>(
		// net.minecraft.world.level.levelgen.feature.Feature.ORE,
		// new OreConfiguration(null, 0)));

		// BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// placedKey);

		// // 1. Регистрируем Configured Feature
		// Registry.register(Registries.CONFIGURED_FEATURE, configuredKey,
		// new ConfiguredFeature<>(
		// Feature.ORE,
		// new OreConfiguration(
		// OreConfiguration.target(OreConfiguration.ORE_REPLACEABLES,
		// ModBlocks.VENOM_BLOCK.defaultBlockState()),
		// 9)));

		// // 2. Регистрируем Placed Feature
		// Registry.register(Registries.PLACED_FEATURE, placedKey,
		// new PlacedFeature(
		// configuredKey,
		// List.of(
		// CountPlacement.of(5),
		// InSquarePlacement.spread(),
		// HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(15),
		// VerticalAnchor.absolute(65)),
		// BiomePlacement.INSTANCE)));

		// 3. Добавляем в биомы
		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// placedKey);

		// public static final RegistryKey<PlacedFeature> CUSTOM_ORE_PLACED_KEY =
		// RegistryKey.of(RegistryKeys.PLACED_FEATURE,
		// Identifier.of("tutorial","ore_custom"));

		// // Configured Feature
		// Registry.register(
		// (Registry<ConfiguredFeature<?, ?>>) Registries.CONFIGURED_FEATURE,
		// configuredKey,
		// new ConfiguredFeature<>(
		// Feature.ORE,
		// new OreConfiguration(
		// OreConfiguration.target(OreConfiguration.ORE_REPLACEABLES,
		// ModBlocks.VENOM_BLOCK.defaultBlockState()),
		// 9)));

		// // Placed Feature
		// Registry.register(
		// (Registry<PlacedFeature>) Registries.PLACED_FEATURE,
		// placedKey,
		// new PlacedFeature(
		// configuredKey,
		// List.of(
		// CountPlacement.of(5),
		// InSquarePlacement.spread(),
		// HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(15),
		// VerticalAnchor.absolute(65)),
		// BiomePlacement.INSTANCE)));

		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// placedKey);

		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ModPlacedFeatures.VENOM_ORE);
	}

	@Override
	public void onInitialize() {
		// System.out.println("Хуепампало!");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			// Создание готовой команды "huepampalo", которая восстанавливает здоровье
			// игрока и отправляет сообщение в чат.
			dispatcher.register(
					net.minecraft.commands.Commands.literal("huepampalo")
							.executes(context -> {
								if (context.getSource().getEntity() instanceof Player player) {
									player.setHealth(player.getMaxHealth());
									player.sendSystemMessage(Component.literal("Хуепампало веном!"));
									return 1;
								}
								return 0;
							}));
		});

		// Регистрация предмета "huepampalo_item" в реестре Minecraft
		Registry.register(
				BuiltInRegistries.ITEM,
				id("huepampalo_item"),
				HUEPAMPALO_ITEM);
		// ModPlayerTick.register();

		ModBlocks.register();
		ModItems.register();

		registerVenomOre();
		// WorldGeneration.generate();
		// ModWorldGen.register();

		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// ModPlacedFeatures.VENOM_BLOCK_KEY);
		// BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
		// Feature.UNDERGROUND_ORES, ModOrePlacedFeatures.ORE_RUBY);

		// registerVenomOre();

		// ModFeatures.register();

		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// ResourceKey.create(
		// Registries.PLACED_FEATURE,
		// HuepampaloMod.id("venom_ore")));

		// System.out.println("Huepampalo Mod initialized with simple ore generation!");

		// Простая генерация Venom Block (самый надёжный способ)
		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// ResourceKey.create(
		// Registries.PLACED_FEATURE,
		// HuepampaloMod.id("venom_ore")));

		// System.out.println("Venom Block simple generation added!");

		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// ModPlacedFeatures.VENOM_PLACED);

		// BiomeModifications.addFeature(
		// BiomeSelectors.foundInOverworld(),
		// GenerationStep.Decoration.UNDERGROUND_ORES,
		// ModPlacedFeatures.VENOM_PLACED_KEY);

	}

	// CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess,
	// environment) -> {

	// // dispatcherl

	// dispatcher.register(
	// CommandManager.literal("huepampalo")
	// .executes(context -> {
	// if (context.getSource().getEntity() instanceof ServerPlayerEntity player) {
	// player.setHealth(player.getMaxHealth());
	// player.sendMessage(Text.literal("§aПолучено полное здоровье!"), false);
	// return 1;
	// }
	// return 0;
	// })
	// );

	// Player.
	// Player.class.("Хуепампало!", false);

	// context.

	// player.huepampalo();

	// This code runs as soon as Minecraft is in a mod-load-ready state.
	// However, some things (like resources) may still be uninitialized.
	// Proceed with mild caution.

	// Русский язык и это перевод

	// LOGGER.info("Hello Fabric world!");

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
