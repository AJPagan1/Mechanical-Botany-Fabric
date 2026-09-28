package com.cmb.mechanical_botany.mixin;

import com.cmb.mechanical_botany.registry.ModFluidTags;
import com.cmb.mechanical_botany.registry.ModFluids;
import com.cmb.mechanical_botany.registry.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntitySwimmableFluidMixin {

    /*
     * ================================================================
     * FALL-DISTANCE STORAGE
     * ================================================================
     */

    @Unique
    private float mechanicalBotany$fallDistanceBeforeFluidUpdate;

    /*
     * ================================================================
     * FLUID HEIGHT / CURRENT DETECTION
     * ================================================================
     */

    @Redirect(
            method = "updateInWaterStateAndDoWaterCurrentPushing",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TagKey<Fluid> mechanicalBotany$useSwimmableFluidForMovement() {
        return ModFluidTags.SWIMMABLE;
    }

    /*
     * ================================================================
     * FORGE-STYLE FALL DISTANCE
     * ================================================================
     */

    @Inject(
            method = "updateInWaterStateAndDoFluidPushing",
            at = @At("HEAD")
    )
    private void mechanicalBotany$captureFallDistanceBeforeFluidUpdate(
            CallbackInfoReturnable<Boolean> cir
    ) {
        Entity entity =
                (Entity) (Object) this;

        mechanicalBotany$fallDistanceBeforeFluidUpdate =
                entity.fallDistance;
    }

    @Inject(
            method = "updateInWaterStateAndDoFluidPushing",
            at = @At("RETURN")
    )
    private void mechanicalBotany$applyCompostFallDistanceModifier(
            CallbackInfoReturnable<Boolean> cir
    ) {
        Entity entity =
                (Entity) (Object) this;

        if (
                mechanicalBotany$intersectsRealWater(
                        entity
                )
        ) {
            return;
        }

        if (
                mechanicalBotany$intersectsBotanyFluid(
                        entity
                )
        ) {
            entity.fallDistance =
                    mechanicalBotany$fallDistanceBeforeFluidUpdate
                            * 0.5F;
        }
    }

    /*
     * ================================================================
     * ITEM BUOYANCY FLUID HEIGHT
     * ================================================================
     *
     * ItemEntity's vanilla water movement checks:
     *
     *     isInWater()
     *     &&
     *     getFluidHeight(FluidTags.WATER) > 0.1F
     *
     * Our general fluid bridge supplies isInWater().
     *
     * When an ItemEntity asks for WATER height, include the actual
     * intersecting Mechanical Botany fluid depth.
     *
     * The important part here is that getBotanyFluidHeight() now
     * recognizes BOTH source and flowing variants.
     */
    @Inject(
            method = "getFluidHeight",
            at = @At("RETURN"),
            cancellable = true
    )
    private void mechanicalBotany$provideItemCompostFluidHeight(
            TagKey<Fluid> fluidTag,
            CallbackInfoReturnable<Double> cir
    ) {
        Entity entity =
                (Entity) (Object) this;

        if (!(entity instanceof ItemEntity)) {
            return;
        }

        if (fluidTag != FluidTags.WATER) {
            return;
        }

        double compostHeight =
                mechanicalBotany$getBotanyFluidHeight(
                        entity
                );

        if (
                compostHeight
                        > cir.getReturnValue()
        ) {
            cir.setReturnValue(
                    compostHeight
            );
        }
    }

    /*
     * ================================================================
     * PREVENT COMPOST FROM EXTINGUISHING FIRE
     * ================================================================
     */

    @Redirect(
            method = "updateInWaterStateAndDoWaterCurrentPushing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;clearFire()V"
            )
    )
    private void mechanicalBotany$onlyRealWaterExtinguishes(
            Entity entity
    ) {
        if (
                mechanicalBotany$intersectsRealWater(
                        entity
                )
        ) {
            entity.clearFire();
        }
    }

    /*
     * ================================================================
     * EYE SUBMERSION
     * ================================================================
     */

    @Redirect(
            method = "updateFluidOnEyes",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TagKey<Fluid> mechanicalBotany$useSwimmableFluidForEyes() {
        return ModFluidTags.SWIMMABLE;
    }

    /*
     * ================================================================
     * SWIMMING START CHECK
     * ================================================================
     */

    @Redirect(
            method = "updateSwimming",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TagKey<Fluid> mechanicalBotany$useSwimmableFluidForSwimming() {
        return ModFluidTags.SWIMMABLE;
    }

    /*
     * ================================================================
     * IS-IN-WATER MOVEMENT BRIDGE
     * ================================================================
     */

    @Inject(
            method = "isInWater",
            at = @At("RETURN"),
            cancellable = true
    )
    private void mechanicalBotany$isInSwimmableFluid(
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (cir.getReturnValue()) {
            return;
        }

        Entity entity =
                (Entity) (Object) this;

        if (
                mechanicalBotany$intersectsBotanyFluid(
                        entity
                )
        ) {
            cir.setReturnValue(true);
        }
    }

    /*
     * ================================================================
     * UNDERWATER BRIDGE
     * ================================================================
     */

    @Inject(
            method = "isUnderWater",
            at = @At("RETURN"),
            cancellable = true
    )
    private void mechanicalBotany$isUnderBotanyFluid(
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (cir.getReturnValue()) {
            return;
        }

        Entity entity =
                (Entity) (Object) this;

        if (
                mechanicalBotany$isEyeInsideBotanyFluid(
                        entity
                )
        ) {
            cir.setReturnValue(true);
        }
    }

    /*
     * ================================================================
     * WETNESS PARITY
     * ================================================================
     */

    @Inject(
            method = "isInWaterRainOrBubble",
            at = @At("RETURN"),
            cancellable = true
    )
    private void mechanicalBotany$removeSyntheticWetness(
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!cir.getReturnValue()) {
            return;
        }

        Entity entity =
                (Entity) (Object) this;

        if (
                !mechanicalBotany$intersectsBotanyFluid(
                        entity
                )
        ) {
            return;
        }

        boolean actuallyWet =
                mechanicalBotany$intersectsRealWater(
                        entity
                )
                        || mechanicalBotany$isActuallyInRain(
                        entity
                )
                        || mechanicalBotany$isActuallyInBubbleColumn(
                        entity
                );

        cir.setReturnValue(
                actuallyWet
        );
    }

    /*
     * ================================================================
     * CUSTOM SPLASH PARTICLES
     * ================================================================
     */

    @Inject(
            method = "doWaterSplashEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mechanicalBotany$replaceWaterSplash(
            CallbackInfo ci
    ) {
        Entity entity =
                (Entity) (Object) this;

        Level level =
                entity.level();

        FluidState fluidState =
                mechanicalBotany$getFluidAroundEntity(
                        entity
                );

        ParticleOptions particle =
                mechanicalBotany$getSplashParticle(
                        fluidState
                );

        if (particle == null) {
            return;
        }

        ci.cancel();

        if (!level.isClientSide) {
            return;
        }

        double horizontalSpeed =
                entity
                        .getDeltaMovement()
                        .horizontalDistance();

        float width =
                entity.getBbWidth();

        int particleCount =
                Math.max(
                        4,
                        Mth.ceil(
                                1.0F
                                        + width * 12.0F
                                        + horizontalSpeed * 20.0D
                        )
                );

        double surfaceY =
                mechanicalBotany$getFluidSurfaceY(
                        entity
                );

        for (
                int i = 0;
                i < particleCount;
                i++
        ) {
            double x =
                    entity.getX()
                            + (
                            level.random.nextDouble()
                                    - 0.5D
                    )
                            * width;

            double z =
                    entity.getZ()
                            + (
                            level.random.nextDouble()
                                    - 0.5D
                    )
                            * width;

            double velocityX =
                    (
                            level.random.nextDouble()
                                    - 0.5D
                    )
                            * 0.15D
                            + entity
                            .getDeltaMovement()
                            .x
                            * 0.25D;

            double velocityY =
                    0.1D
                            + level.random.nextDouble()
                            * 0.2D;

            double velocityZ =
                    (
                            level.random.nextDouble()
                                    - 0.5D
                    )
                            * 0.15D
                            + entity
                            .getDeltaMovement()
                            .z
                            * 0.25D;

            level.addParticle(
                    particle,
                    x,
                    surfaceY,
                    z,
                    velocityX,
                    velocityY,
                    velocityZ
            );
        }
    }

    /*
     * ================================================================
     * COMPOST FLUID HEIGHT
     * ================================================================
     */

    @Unique
    private double mechanicalBotany$getBotanyFluidHeight(
            Entity entity
    ) {
        AABB box =
                entity
                        .getBoundingBox()
                        .deflate(
                                0.001D
                        );

        int minX =
                Mth.floor(
                        box.minX
                );

        int maxX =
                Mth.floor(
                        box.maxX
                );

        int minY =
                Mth.floor(
                        box.minY
                );

        int maxY =
                Mth.floor(
                        box.maxY
                );

        int minZ =
                Mth.floor(
                        box.minZ
                );

        int maxZ =
                Mth.floor(
                        box.maxZ
                );

        Level level =
                entity.level();

        BlockPos.MutableBlockPos pos =
                new BlockPos.MutableBlockPos();

        double highestDepth =
                0.0D;

        for (
                int x = minX;
                x <= maxX;
                x++
        ) {
            for (
                    int y = minY;
                    y <= maxY;
                    y++
            ) {
                for (
                        int z = minZ;
                        z <= maxZ;
                        z++
                ) {
                    pos.set(
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
                                    state
                            )
                    ) {
                        continue;
                    }

                    double fluidSurface =
                            y
                                    + state.getHeight(
                                    level,
                                    pos
                            );

                    if (
                            fluidSurface
                                    <= box.minY
                    ) {
                        continue;
                    }

                    double depth =
                            fluidSurface
                                    - box.minY;

                    if (
                            depth
                                    > highestDepth
                    ) {
                        highestDepth =
                                depth;
                    }
                }
            }
        }

        return highestDepth;
    }

    /*
     * ================================================================
     * MECHANICAL BOTANY FLUID INTERSECTION
     * ================================================================
     */

    @Unique
    private boolean mechanicalBotany$intersectsBotanyFluid(
            Entity entity
    ) {
        return mechanicalBotany$getBotanyFluidHeight(
                entity
        ) > 0.0D;
    }

    /*
     * ================================================================
     * REAL VANILLA WATER INTERSECTION
     * ================================================================
     */

    @Unique
    private boolean mechanicalBotany$intersectsRealWater(
            Entity entity
    ) {
        AABB box =
                entity
                        .getBoundingBox()
                        .deflate(
                                0.001D
                        );

        int minX =
                Mth.floor(
                        box.minX
                );

        int maxX =
                Mth.floor(
                        box.maxX
                );

        int minY =
                Mth.floor(
                        box.minY
                );

        int maxY =
                Mth.floor(
                        box.maxY
                );

        int minZ =
                Mth.floor(
                        box.minZ
                );

        int maxZ =
                Mth.floor(
                        box.maxZ
                );

        Level level =
                entity.level();

        BlockPos.MutableBlockPos pos =
                new BlockPos.MutableBlockPos();

        for (
                int x = minX;
                x <= maxX;
                x++
        ) {
            for (
                    int y = minY;
                    y <= maxY;
                    y++
            ) {
                for (
                        int z = minZ;
                        z <= maxZ;
                        z++
                ) {
                    pos.set(
                            x,
                            y,
                            z
                    );

                    FluidState state =
                            level.getFluidState(
                                    pos
                            );

                    if (
                            !state.is(
                                    FluidTags.WATER
                            )
                    ) {
                        continue;
                    }

                    double fluidSurface =
                            y
                                    + state.getHeight(
                                    level,
                                    pos
                            );

                    if (
                            box.minY
                                    < fluidSurface
                    ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /*
     * ================================================================
     * ACTUAL RAIN
     * ================================================================
     */

    @Unique
    private boolean mechanicalBotany$isActuallyInRain(
            Entity entity
    ) {
        Level level =
                entity.level();

        BlockPos bottom =
                entity.blockPosition();

        if (
                level.isRainingAt(
                        bottom
                )
        ) {
            return true;
        }

        BlockPos top =
                BlockPos.containing(
                        entity.getX(),
                        entity
                                .getBoundingBox()
                                .maxY,
                        entity.getZ()
                );

        return level.isRainingAt(
                top
        );
    }

    /*
     * ================================================================
     * ACTUAL BUBBLE COLUMN
     * ================================================================
     */

    @Unique
    private boolean mechanicalBotany$isActuallyInBubbleColumn(
            Entity entity
    ) {
        AABB box =
                entity
                        .getBoundingBox()
                        .deflate(
                                0.001D
                        );

        Level level =
                entity.level();

        int minX =
                Mth.floor(
                        box.minX
                );

        int maxX =
                Mth.floor(
                        box.maxX
                );

        int minY =
                Mth.floor(
                        box.minY
                );

        int maxY =
                Mth.floor(
                        box.maxY
                );

        int minZ =
                Mth.floor(
                        box.minZ
                );

        int maxZ =
                Mth.floor(
                        box.maxZ
                );

        BlockPos.MutableBlockPos pos =
                new BlockPos.MutableBlockPos();

        for (
                int x = minX;
                x <= maxX;
                x++
        ) {
            for (
                    int y = minY;
                    y <= maxY;
                    y++
            ) {
                for (
                        int z = minZ;
                        z <= maxZ;
                        z++
                ) {
                    pos.set(
                            x,
                            y,
                            z
                    );

                    if (
                            level
                                    .getBlockState(
                                            pos
                                    )
                                    .is(
                                            Blocks.BUBBLE_COLUMN
                                    )
                    ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /*
     * ================================================================
     * EYE DETECTION
     * ================================================================
     */

    @Unique
    private boolean mechanicalBotany$isEyeInsideBotanyFluid(
            Entity entity
    ) {
        Level level =
                entity.level();

        double eyeY =
                entity.getEyeY();

        BlockPos pos =
                BlockPos.containing(
                        entity.getX(),
                        eyeY,
                        entity.getZ()
                );

        FluidState state =
                level.getFluidState(
                        pos
                );

        if (
                !mechanicalBotany$isBotanyFluid(
                        state
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

        return eyeY
                < fluidSurface;
    }

    /*
     * ================================================================
     * BOTANY FLUID FAMILY CHECK
     * ================================================================
     *
     * THIS IS THE IMPORTANT FIX.
     *
     * Minecraft registers source and flowing fluids as separate Fluid
     * instances.
     *
     * Comparing the FluidState's exact type to only the registered
     * source fluid therefore fails for flowing blocks.
     *
     * Every Mechanical Botany placeable fluid is a FlowingFluid.
     *
     * Normalize whichever variant is present:
     *
     *     source variant  -> source
     *     flowing variant -> source
     *
     * and compare the normalized source identities.
     *
     * This allows source AND flowing blocks to be treated as the same
     * fluid family.
     */
    @Unique
    private boolean mechanicalBotany$isBotanyFluid(
            FluidState state
    ) {
        Fluid fluid =
                state.getType();

        if (!(fluid instanceof FlowingFluid flowingFluid)) {
            return false;
        }

        Fluid source =
                flowingFluid.getSource();

        return source
                == ModFluids.COMPOST
                .get()
                .getSource()

                || source
                == ModFluids.MOLTEN_COMPOST
                .get()
                .getSource()

                || source
                == ModFluids.VOID_COMPOST
                .get()
                .getSource();
    }

    /*
     * ================================================================
     * SPLASH HELPERS
     * ================================================================
     */

    @Unique
    private FluidState mechanicalBotany$getFluidAroundEntity(
            Entity entity
    ) {
        Level level =
                entity.level();

        BlockPos current =
                BlockPos.containing(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ()
                );

        FluidState state =
                level.getFluidState(
                        current
                );

        if (
                mechanicalBotany$isBotanyFluid(
                        state
                )
        ) {
            return state;
        }

        return level.getFluidState(
                current.below()
        );
    }

    @Unique
    private double mechanicalBotany$getFluidSurfaceY(
            Entity entity
    ) {
        Level level =
                entity.level();

        BlockPos pos =
                BlockPos.containing(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ()
                );

        FluidState state =
                level.getFluidState(
                        pos
                );

        if (
                !mechanicalBotany$isBotanyFluid(
                        state
                )
        ) {
            pos =
                    pos.below();

            state =
                    level.getFluidState(
                            pos
                    );
        }

        if (
                mechanicalBotany$isBotanyFluid(
                        state
                )
        ) {
            return pos.getY()
                    + state.getHeight(
                    level,
                    pos
            );
        }

        return entity.getY();
    }

    /*
     * ================================================================
     * PARTICLE FLUID FAMILY
     * ================================================================
     *
     * Normalize source/flowing exactly like isBotanyFluid() so flowing
     * Compost also gets the correct custom splash particle.
     */
    @Unique
    private ParticleOptions mechanicalBotany$getSplashParticle(
            FluidState state
    ) {
        Fluid fluid =
                state.getType();

        if (!(fluid instanceof FlowingFluid flowingFluid)) {
            return null;
        }

        Fluid source =
                flowingFluid.getSource();

        if (
                source
                        == ModFluids.COMPOST
                        .get()
                        .getSource()
        ) {
            return ModParticles.COMPOST_SPLASH;
        }

        if (
                source
                        == ModFluids.MOLTEN_COMPOST
                        .get()
                        .getSource()
        ) {
            return ModParticles.MOLTEN_COMPOST_SPLASH;
        }

        if (
                source
                        == ModFluids.VOID_COMPOST
                        .get()
                        .getSource()
        ) {
            return ModParticles.VOID_COMPOST_SPLASH;
        }

        return null;
    }
}