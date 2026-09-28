package com.cmb.mechanical_botany.ponder;

import com.cmb.mechanical_botany.registry.ModBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class ModPonderTags {

    private ModPonderTags() {
    }

    public static void register(
            PonderTagRegistrationHelper<ResourceLocation> helper
    ) {
        PonderTagRegistrationHelper<Block> blockHelper =
                helper.withKeyFunction(
                        BuiltInRegistries.BLOCK::getKey
                );

        blockHelper
                .addToTag(
                        AllCreatePonderTags.KINETIC_APPLIANCES
                )
                .add(
                        ModBlocks.MECHANICAL_INSOLATOR
                )
                .add(
                        ModBlocks.MECHANICAL_COMPOSTER
                );

        blockHelper
                .addToTag(
                        AllCreatePonderTags.ARM_TARGETS
                )
                .add(
                        ModBlocks.MECHANICAL_INSOLATOR
                )
                .add(
                        ModBlocks.MECHANICAL_COMPOSTER
                );
    }
}