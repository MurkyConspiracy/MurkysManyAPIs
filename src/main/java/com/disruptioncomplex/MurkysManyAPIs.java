package com.disruptioncomplex;

import com.disruptioncomplex.common.item.EnchantmentItemGroup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MurkysManyAPIs implements ModInitializer {
	public static final String MOD_ID = "murkys-many-apis";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		EnchantmentItemGroup.registerItemGroups();

		// Get version from FabricLoader
		LOGGER.info("Murky has Many APIs! This one is version: {}", FabricLoader.getInstance().getModContainer(MOD_ID).isPresent() ? FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString(): "Unknown?!?!");

	}
}