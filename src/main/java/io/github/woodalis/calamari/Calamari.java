package io.github.woodalis.calamari;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Calamari implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Calamari");

	private static final Identifier SQUID_LOOT_TABLE_ID = EntityType.SQUID.getLootTableId();

	private static final Identifier GLOW_SQUID_LOOT_TABLE_ID = EntityType.GLOW_SQUID.getLootTableId();

	public static final Item SQUID_FILLET = new Item(new QuiltItemSettings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.2f).build()));

	public static final Item SQUID_TENTACLE = new Item(new QuiltItemSettings().food(new FoodComponent.Builder().hunger(1).saturationModifier(0.2f).build()));

	public static final Item CALAMARI = new Item(new QuiltItemSettings().food(new FoodComponent.Builder().hunger(5).saturationModifier(1.2f).build()));

	public static final Item CRISPY_TENTACLE = new Item(new QuiltItemSettings().food(new FoodComponent.Builder().hunger(3).saturationModifier(1.2f).build()));

	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "squid_fillet"), SQUID_FILLET);
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "squid_tentacle"), SQUID_TENTACLE);
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "calamari"), CALAMARI);
		Registry.register(Registries.ITEM, new Identifier(mod.metadata().id(), "crispy_tentacle"), CRISPY_TENTACLE);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> content.addAfter(Items.PUFFERFISH, SQUID_FILLET));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> content.addAfter(SQUID_FILLET, CALAMARI));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> content.addAfter(CALAMARI, SQUID_TENTACLE));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> content.addAfter(SQUID_TENTACLE, CRISPY_TENTACLE));

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && (SQUID_LOOT_TABLE_ID.equals(id) || GLOW_SQUID_LOOT_TABLE_ID.equals(id))) {
				tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(SQUID_FILLET)).rolls(UniformLootNumberProvider.create(0f, 2f)))
						.pool(LootPool.builder().with(ItemEntry.builder(SQUID_TENTACLE)).rolls(UniformLootNumberProvider.create(0f, 4f)));
			}
		});

		LOGGER.info("Kraken awakened!");
	}
}
