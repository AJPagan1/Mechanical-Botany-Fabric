package com.cmb.mechanical_botany.compat.jei;

import com.cmb.mechanical_botany.client.ModPartialModels;
import com.cmb.mechanical_botany.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedMechanicalInsolator
        extends AnimatedKinetics {

    @Override
    public void draw(
            GuiGraphics graphics,
            int xOffset,
            int yOffset
    ) {
        PoseStack poseStack =
                graphics.pose();

        poseStack.pushPose();

        poseStack.translate(
                xOffset,
                yOffset,
                0
        );

        /*
         * ================================================================
         * SHADOW
         * ================================================================
         */

        AllGuiTextures.JEI_SHADOW.render(
                graphics,
                -2,
                30
        );

        /*
         * ================================================================
         * MACHINE
         * ================================================================
         */

        poseStack.translate(
                12,
                35,
                0
        );

        int scale =
                22;

        /*
         * ================================================================
         * ROTATING INSOLATOR COG
         * ================================================================
         *
         * This uses our pre-rotated Mechanical Pump cog.
         *
         * The model itself is already lying horizontally, with its
         * rotational axle on the Y axis. That means JEI only needs to
         * animate the Y rotation.
         *
         * We no longer need to combine a 90-degree orientation change
         * with the animation itself.
         */

        blockElement(
                ModPartialModels.MECHANICAL_PUMP_COG_ROT_90
        )
                .rotateBlock(
                        22.5,
                        getCurrentAngle() * 2,
                        0
                )
                .scale(
                        scale
                )
                .render(
                        graphics
                );

        /*
         * ================================================================
         * MECHANICAL INSOLATOR BODY
         * ================================================================
         */

        BlockState insolatorState =
                ModBlocks.MECHANICAL_INSOLATOR
                        .defaultBlockState();

        blockElement(
                insolatorState
        )
                .rotateBlock(
                        22.5,
                        22.5,
                        0
                )
                .scale(
                        scale
                )
                .render(
                        graphics
                );

        poseStack.popPose();
    }
}