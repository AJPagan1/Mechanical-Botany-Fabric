package com.cmb.mechanical_botany.mixin;

import com.cmb.mechanical_botany.registry.ModFluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntitySwimmableFluidMixin {

    /*
     * ================================================================
     * FLUID JUMP / ASCENT
     * ================================================================
     */
    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TagKey<Fluid> mechanicalBotany$useSwimmableFluidForJumping() {
        return ModFluidTags.SWIMMABLE;
    }

    /*
     * ================================================================
     * BREATHING / DROWNING
     * ================================================================
     */
    @Redirect(
            method = "baseTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private TagKey<Fluid> mechanicalBotany$useSwimmableFluidForDrowning() {
        return ModFluidTags.SWIMMABLE;
    }
}
