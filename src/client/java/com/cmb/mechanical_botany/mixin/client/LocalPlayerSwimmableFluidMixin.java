package com.cmb.mechanical_botany.mixin.client;

import com.cmb.mechanical_botany.registry.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerSwimmableFluidMixin {

    @Shadow
    public Input input;

    @Shadow
    @Final
    protected Minecraft minecraft;

    /*
     * ================================================================
     * CLIENT SWIM-SPRINT BRIDGE
     * ================================================================
     *
     * Vanilla LocalPlayer has additional water-specific sprint checks.
     *
     * Without this bridge, the player can interact with the fluid but
     * cannot begin vanilla's sprint-swimming state in a custom fluid.
     *
     * This portion has already been confirmed working in-game.
     */
    @Inject(
            method = "aiStep",
            at = @At("TAIL")
    )
    private void mechanicalBotany$handleBotanyFluidSwimming(
            CallbackInfo ci
    ) {
        LocalPlayer player =
                (LocalPlayer) (Object) this;

        if (
                !mechanicalBotany$isInsideBotanyFluid(
                        player
                )
        ) {
            return;
        }

        if (
                player.getAbilities().flying
                        || player.isPassenger()
        ) {
            return;
        }

        boolean eyeSubmerged =
                mechanicalBotany$isEyeInsideBotanyFluid(
                        player
                );

        boolean sprintKey =
                minecraft
                        .options
                        .keySprint
                        .isDown();

        boolean movingForward =
                input.hasForwardImpulse();

        /*
         * ================================================================
         * START SPRINTING
         * ================================================================
         */

        if (
                !player.isSprinting()
                        && sprintKey
                        && movingForward
                        && eyeSubmerged
                        && !player.isUsingItem()
                        && !player.hasEffect(
                        MobEffects.BLINDNESS
                )
        ) {
            player.setSprinting(
                    true
            );
        }

        /*
         * ================================================================
         * START / CONTINUE SWIMMING
         * ================================================================
         */

        if (
                player.isSprinting()
                        && eyeSubmerged
                        && movingForward
        ) {
            player.setSwimming(
                    true
            );
        }

        if (
                player.isSwimming()
                        && player.isSprinting()
                        && mechanicalBotany$isInsideBotanyFluid(
                        player
                )
        ) {
            player.setSwimming(
                    true
            );
        }
    }

    /*
     * ================================================================
     * FLUID DETECTION
     * ================================================================
     */

    @Unique
    private boolean mechanicalBotany$isInsideBotanyFluid(
            LocalPlayer player
    ) {
        Level level =
                player.level();

        double minY =
                player
                        .getBoundingBox()
                        .minY
                        + 0.001D;

        double middleY =
                (
                        player
                                .getBoundingBox()
                                .minY
                                + player
                                .getBoundingBox()
                                .maxY
                )
                        * 0.5D;

        double maxY =
                player
                        .getBoundingBox()
                        .maxY
                        - 0.001D;

        return mechanicalBotany$isInsideFluidAt(
                level,
                player.getX(),
                minY,
                player.getZ()
        )
                || mechanicalBotany$isInsideFluidAt(
                level,
                player.getX(),
                middleY,
                player.getZ()
        )
                || mechanicalBotany$isInsideFluidAt(
                level,
                player.getX(),
                maxY,
                player.getZ()
        );
    }

    @Unique
    private boolean mechanicalBotany$isEyeInsideBotanyFluid(
            LocalPlayer player
    ) {
        return mechanicalBotany$isInsideFluidAt(
                player.level(),
                player.getX(),
                player.getEyeY(),
                player.getZ()
        );
    }

    @Unique
    private boolean mechanicalBotany$isInsideFluidAt(
            Level level,
            double x,
            double y,
            double z
    ) {
        BlockPos pos =
                BlockPos.containing(
                        x,
                        y,
                        z
                );

        FluidState state =
                level.getFluidState(
                        pos
                );

        if (
                !mechanicalBotany$isBotanyFluid(
                        state.getType()
                )
        ) {
            return false;
        }

        double fluidSurface =
                pos.getY()
                        + state.getHeight(
                        level,
                        pos
                );

        return y < fluidSurface;
    }

    @Unique
    private boolean mechanicalBotany$isBotanyFluid(
            Fluid fluid
    ) {
        return fluid
                == ModFluids.COMPOST.get()
                || fluid
                == ModFluids.COMPOST
                .get()
                .getSource()

                || fluid
                == ModFluids.MOLTEN_COMPOST.get()
                || fluid
                == ModFluids.MOLTEN_COMPOST
                .get()
                .getSource()

                || fluid
                == ModFluids.VOID_COMPOST.get()
                || fluid
                == ModFluids.VOID_COMPOST
                .get()
                .getSource();
    }
}