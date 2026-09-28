package com.cmb.mechanical_botany.kinetics.composter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class MechanicalComposterRenderer
        extends KineticBlockEntityRenderer<MechanicalComposterBlockEntity> {

    public MechanicalComposterRenderer(
            BlockEntityRendererProvider.Context context
    ) {
        super(context);
    }

    @Override
    protected void renderSafe(
            MechanicalComposterBlockEntity blockEntity,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay
    ) {
        if (VisualizationManager.supportsVisualization(
                blockEntity.getLevel()
        )) {
            return;
        }

        BlockState blockState = blockEntity.getBlockState();

        VertexConsumer vertexConsumer =
                buffer.getBuffer(RenderType.solid());

        SuperByteBuffer cogwheel =
                CachedBuffers.partial(
                        AllPartialModels.COGWHEEL,
                        blockState
                );

        standardKineticRotationTransform(
                cogwheel,
                blockEntity,
                light
        ).renderInto(
                poseStack,
                vertexConsumer
        );
    }
}