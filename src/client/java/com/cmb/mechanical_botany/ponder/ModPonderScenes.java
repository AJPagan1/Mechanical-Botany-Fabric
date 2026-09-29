package com.cmb.mechanical_botany.ponder;

import com.cmb.mechanical_botany.kinetics.composter.ComposterPonderScenes;
import com.cmb.mechanical_botany.kinetics.insolator.InsolatorPonderScenes;
import com.cmb.mechanical_botany.registry.ModBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class ModPonderScenes {

    private ModPonderScenes() {
    }

    public static void register(
            PonderSceneRegistrationHelper<ResourceLocation> helper
    ) {
        PonderSceneRegistrationHelper<Block> blockHelper =
                helper.withKeyFunction(
                        BuiltInRegistries.BLOCK::getKey
                );

        /*
         * ================================================================
         * MECHANICAL INSOLATOR
         * ================================================================
         */

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_INSOLATOR
                )
                .addStoryBoard(
                        "insolator",
                        new InsolatorPonderScenes.Intro()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_INSOLATOR
                )
                .addStoryBoard(
                        "compost_use",
                        new InsolatorPonderScenes.CompostUsage()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_INSOLATOR
                )
                .addStoryBoard(
                        "molten_compost_use",
                        new InsolatorPonderScenes.MoltenCompostUsage()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_INSOLATOR
                )
                .addStoryBoard(
                        "void_compost_use",
                        new InsolatorPonderScenes.VoidCompostUsage()
                );

        /*
         * ================================================================
         * MECHANICAL COMPOSTER
         * ================================================================
         */

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_COMPOSTER
                )
                .addStoryBoard(
                        "composter",
                        new ComposterPonderScenes.Intro()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_COMPOSTER
                )
                .addStoryBoard(
                        "layered_composter",
                        new ComposterPonderScenes.LayeredComposters()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_COMPOSTER
                )
                .addStoryBoard(
                        "compost_creation",
                        new ComposterPonderScenes.CreatingLiquidCompost()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_COMPOSTER
                )
                .addStoryBoard(
                        "compost_use",
                        new InsolatorPonderScenes.CompostUsage()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_COMPOSTER
                )
                .addStoryBoard(
                        "molten_compost_use",
                        new InsolatorPonderScenes.MoltenCompostUsage()
                );

        blockHelper
                .forComponents(
                        ModBlocks.MECHANICAL_COMPOSTER
                )
                .addStoryBoard(
                        "void_compost_use",
                        new InsolatorPonderScenes.VoidCompostUsage()
                );
    }
}
