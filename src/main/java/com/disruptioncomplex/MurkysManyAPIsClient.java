package com.disruptioncomplex;

import net.fabricmc.api.ClientModInitializer;

@SuppressWarnings("unused")
public class MurkysManyAPIsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MurkysManyAPIs.LOGGER.info("Murky's Many APIs Client!");
    }
}
