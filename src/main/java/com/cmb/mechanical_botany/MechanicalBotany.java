package com.cmb.mechanical_botany;

import com.cmb.mechanical_botany.registry.ModArmInteractionPointTypes;
import com.cmb.mechanical_botany.registry.ModBlockEntities;
import com.cmb.mechanical_botany.registry.ModBlocks;
import com.cmb.mechanical_botany.registry.ModFluids;
import com.cmb.mechanical_botany.registry.ModItems;
import com.cmb.mechanical_botany.registry.ModParticles;
import com.cmb.mechanical_botany.registry.ModRecipeTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MechanicalBotany implements ModInitializer {

    public static final String MOD_ID =
            "mechanical_botany";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(
                    MOD_ID
            );

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(
                    MOD_ID
            );

    @Override
    public void onInitialize() {
        LOGGER.info(
                "Initializing Create: Mechanical Botany."
        );

        /*
         * ================================================================
         * CONFIG
         * ================================================================
         */

        ModConfigs.register();

        /*
         * ================================================================
         * NORMAL CONTENT
         * ================================================================
         */

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModRecipeTypes.register();
        ModParticles.register();

        /*
         * ================================================================
         * CREATE INTEGRATION
         * ================================================================
         */

        ModArmInteractionPointTypes.register();

        /*
         * ================================================================
         * REGISTRATE CONTENT
         * ================================================================
         */

        ModFluids.register();

        REGISTRATE.register();

        LOGGER.info(
                "Create: Mechanical Botany initialized."
        );
    }

    public static ResourceLocation id(
            String path
    ) {
        return new ResourceLocation(
                MOD_ID,
                path
        );
    }
}