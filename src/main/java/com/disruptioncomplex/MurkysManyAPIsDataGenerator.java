package com.disruptioncomplex;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * Data generator class for Murky's Many APIs mod.
 * Implements the Fabric DataGeneratorEntrypoint interface to handle data generation during mod development.
 */
@SuppressWarnings("unused")
public class MurkysManyAPIsDataGenerator implements DataGeneratorEntrypoint {

    /**
     * Default constructor for MurkysManyAPIsDataGenerator.
     * Initializes a new instance of the data generator.
     */
    public MurkysManyAPIsDataGenerator() {
    }

    /**
     * Initializes the data generator for the mod.
     * This method is called by Fabric's data generation system to set up data generation for the mod.
     *
     * @param fabricDataGenerator The Fabric data generator instance provided by the framework
     */
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    }
}
