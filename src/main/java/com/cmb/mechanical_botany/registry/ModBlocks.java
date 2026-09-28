package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.ModConfigs;
import com.cmb.mechanical_botany.kinetics.composter.MechanicalComposterBlock;
import com.cmb.mechanical_botany.kinetics.insolator.MechanicalInsolatorBlock;
import com.simibubi.create.api.stress.BlockStressValues;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.DoubleSupplier;

public final class ModBlocks {

    public static final MechanicalComposterBlock MECHANICAL_COMPOSTER =
            new MechanicalComposterBlock(
                    BlockBehaviour.Properties
                            .copy(
                                    Blocks.COPPER_BLOCK
                            )
                            .strength(
                                    3.0F,
                                    6.0F
                            )
                            .sound(
                                    SoundType.COPPER
                            )
                            .noOcclusion()
                            .requiresCorrectToolForDrops()
            );

    public static final MechanicalInsolatorBlock MECHANICAL_INSOLATOR =
            new MechanicalInsolatorBlock(
                    BlockBehaviour.Properties
                            .copy(
                                    Blocks.COPPER_BLOCK
                            )
                            .strength(
                                    3.0F,
                                    6.0F
                            )
                            .sound(
                                    SoundType.COPPER
                            )
                            .noOcclusion()
                            .requiresCorrectToolForDrops()
            );

    private ModBlocks() {
    }

    public static void register() {

        Registry.register(
                BuiltInRegistries.BLOCK,
                MechanicalBotany.id(
                        "mechanical_composter"
                ),
                MECHANICAL_COMPOSTER
        );

        Registry.register(
                BuiltInRegistries.BLOCK,
                MechanicalBotany.id(
                        "mechanical_insolator"
                ),
                MECHANICAL_INSOLATOR
        );

        /*
         * ================================================================
         * CONFIGURABLE CREATE STRESS IMPACT
         * ================================================================
         *
         * Exact 1.0.5 defaults:
         *
         * Composter = 16
         * Insolator = 64
         */

        BlockStressValues.IMPACTS.registerProvider(
                ModBlocks::getStressImpact
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany blocks."
        );
    }

    private static DoubleSupplier getStressImpact(
            Block block
    ) {
        if (
                block
                        == MECHANICAL_INSOLATOR
        ) {
            return () ->
                    ModConfigs
                            .server()
                            .insolator
                            .kineticStressImpact
                            .get();
        }

        if (
                block
                        == MECHANICAL_COMPOSTER
        ) {
            return () ->
                    ModConfigs
                            .server()
                            .composter
                            .kineticStressImpact
                            .get();
        }

        return null;
    }
}