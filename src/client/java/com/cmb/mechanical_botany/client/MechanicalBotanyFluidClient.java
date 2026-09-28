package com.cmb.mechanical_botany.client;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.registry.ModFluids;
import com.mojang.blaze3d.shaders.FogShape;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public final class MechanicalBotanyFluidClient {

    private static final int COMPOST_FOG =
            0x523C18;

    private static final int MOLTEN_COMPOST_FOG =
            0x442814;

    private static final int VOID_COMPOST_FOG =
            0x432652;

    private MechanicalBotanyFluidClient() {
    }

    public static void register() {

        registerFluidRenderers();
        registerFog();

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany fluid rendering."
        );
    }

    private static void registerFluidRenderers() {

        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.COMPOST
                        .get()
                        .getSource(),
                ModFluids.COMPOST.get(),
                new SimpleFluidRenderHandler(
                        MechanicalBotany.id(
                                "fluid/compost_still"
                        ),
                        MechanicalBotany.id(
                                "fluid/compost_flow"
                        )
                )
        );

        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.MOLTEN_COMPOST
                        .get()
                        .getSource(),
                ModFluids.MOLTEN_COMPOST.get(),
                new SimpleFluidRenderHandler(
                        MechanicalBotany.id(
                                "fluid/molten_compost_still"
                        ),
                        MechanicalBotany.id(
                                "fluid/molten_compost_flow"
                        )
                )
        );

        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.VOID_COMPOST
                        .get()
                        .getSource(),
                ModFluids.VOID_COMPOST.get(),
                new SimpleFluidRenderHandler(
                        MechanicalBotany.id(
                                "fluid/void_compost_still"
                        ),
                        MechanicalBotany.id(
                                "fluid/void_compost_flow"
                        )
                )
        );

        BlockRenderLayerMap.INSTANCE.putFluids(
                RenderType.translucent(),

                ModFluids.COMPOST.get(),
                ModFluids.COMPOST
                        .get()
                        .getSource(),

                ModFluids.MOLTEN_COMPOST.get(),
                ModFluids.MOLTEN_COMPOST
                        .get()
                        .getSource(),

                ModFluids.VOID_COMPOST.get(),
                ModFluids.VOID_COMPOST
                        .get()
                        .getSource()
        );
    }

    private static void registerFog() {

        FogEvents.SET_COLOR.register(
                (data, partialTick) -> {

                    int color =
                            getFogColor(
                                    data.getCamera()
                            );

                    if (color == -1) {
                        return;
                    }

                    data.setRed(
                            ((color >> 16) & 255)
                                    / 255.0F
                    );

                    data.setGreen(
                            ((color >> 8) & 255)
                                    / 255.0F
                    );

                    data.setBlue(
                            (color & 255)
                                    / 255.0F
                    );
                }
        );

        FogEvents.RENDER_FOG.register(
                (
                        mode,
                        fogType,
                        camera,
                        partialTick,
                        renderDistance,
                        nearDistance,
                        farDistance,
                        fogShape,
                        fogData
                ) -> {

                    if (
                            getFogColor(
                                    camera
                            )
                                    == -1
                    ) {
                        return false;
                    }

                    float fogDistanceModifier =
                            0.125F
                                    * AllConfigs
                                    .client()
                                    .honeyTransparencyMultiplier
                                    .getF();

                    fogData.setNearPlaneDistance(
                            -8.0F
                    );

                    fogData.setFarPlaneDistance(
                            96.0F
                                    * fogDistanceModifier
                    );

                    fogData.setFogShape(
                            FogShape.CYLINDER
                    );

                    return true;
                }
        );
    }

    private static int getFogColor(
            Camera camera
    ) {
        Fluid fluid =
                getFluidAtCamera(
                        camera
                );

        if (
                isFluid(
                        fluid,
                        ModFluids.COMPOST.get()
                )
        ) {
            return COMPOST_FOG;
        }

        if (
                isFluid(
                        fluid,
                        ModFluids.MOLTEN_COMPOST.get()
                )
        ) {
            return MOLTEN_COMPOST_FOG;
        }

        if (
                isFluid(
                        fluid,
                        ModFluids.VOID_COMPOST.get()
                )
        ) {
            return VOID_COMPOST_FOG;
        }

        return -1;
    }

    private static Fluid getFluidAtCamera(
            Camera camera
    ) {
        if (
                !(
                        camera
                                .getEntity()
                                .level()
                                instanceof ClientLevel level
                )
        ) {
            return Fluids.EMPTY;
        }

        BlockPos pos =
                BlockPos.containing(
                        camera.getPosition()
                );

        return level
                .getFluidState(
                        pos
                )
                .getType();
    }

    private static boolean isFluid(
            Fluid fluid,
            Fluid flowing
    ) {
        if (
                fluid == flowing
        ) {
            return true;
        }

        if (
                flowing
                        instanceof FlowingFluid flowingFluid
        ) {
            return fluid
                    == flowingFluid.getSource();
        }

        return false;
    }
}