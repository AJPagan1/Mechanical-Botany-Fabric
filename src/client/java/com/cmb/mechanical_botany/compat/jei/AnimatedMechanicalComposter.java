package com.cmb.mechanical_botany.compat.jei;

import com.cmb.mechanical_botany.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedMechanicalComposter
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
         * Rotating Mechanical Composter cog.
         */

        blockElement(
                AllPartialModels.COGWHEEL
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
         * Mechanical Composter body.
         */

        BlockState composterState =
                ModBlocks.MECHANICAL_COMPOSTER
                        .defaultBlockState();

        blockElement(
                composterState
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