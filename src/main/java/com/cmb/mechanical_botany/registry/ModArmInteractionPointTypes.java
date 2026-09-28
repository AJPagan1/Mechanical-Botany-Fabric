package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ModArmInteractionPointTypes {

    private ModArmInteractionPointTypes() {
    }

    /*
     * ================================================================
     * REGISTRATION
     * ================================================================
     */

    public static void register() {

        Registry.register(
                CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE,
                MechanicalBotany.id(
                        "insolator"
                ),
                new InsolatorType()
        );

        Registry.register(
                CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE,
                MechanicalBotany.id(
                        "composter"
                ),
                new ComposterType()
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany arm interaction point types."
        );
    }

    /*
     * ================================================================
     * MECHANICAL INSOLATOR
     * ================================================================
     */

    private static final class InsolatorType
            extends ArmInteractionPointType {

        @Override
        public boolean canCreatePoint(
                Level level,
                BlockPos pos,
                BlockState state
        ) {
            return state.is(
                    ModBlocks.MECHANICAL_INSOLATOR
            );
        }

        @Override
        public ArmInteractionPoint createPoint(
                Level level,
                BlockPos pos,
                BlockState state
        ) {
            return new ArmInteractionPoint(
                    this,
                    level,
                    pos,
                    state
            );
        }
    }

    /*
     * ================================================================
     * MECHANICAL COMPOSTER
     * ================================================================
     */

    private static final class ComposterType
            extends ArmInteractionPointType {

        @Override
        public boolean canCreatePoint(
                Level level,
                BlockPos pos,
                BlockState state
        ) {
            return state.is(
                    ModBlocks.MECHANICAL_COMPOSTER
            );
        }

        @Override
        public ArmInteractionPoint createPoint(
                Level level,
                BlockPos pos,
                BlockState state
        ) {
            return new ArmInteractionPoint(
                    this,
                    level,
                    pos,
                    state
            );
        }
    }
}