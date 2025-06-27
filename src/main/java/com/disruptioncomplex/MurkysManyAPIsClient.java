package com.disruptioncomplex;

import net.fabricmc.api.ClientModInitializer;

/**
 * Client-side initialization class for Murky's Many APIs mod.
 * Handles client-specific setup and initialization tasks.
 * Implements {@link ClientModInitializer} to provide client-side mod initialization.
 */
@SuppressWarnings("unused")
public class MurkysManyAPIsClient implements ClientModInitializer {
    /**
     * Default constructor for MurkysManyAPIsClient.
     * Initializes a new instance of the mod's client-side class.
     */
    public MurkysManyAPIsClient() {
    }

    @Override
    public void onInitializeClient() {
        MurkysManyAPIs.LOGGER.info("Murky's Many APIs Client!");
    }
}
