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
     *
     * Vanilla LivingEntity.aiStep() specifically asks for
     * FluidTags.WATER when deciding whether holding Jump should keep
     * pushing the entity upward.
     *
     * Mechanical Botany's fluids are tracked under our SWIMMABLE tag,
     * so substitute that tag here.
     *
     * SWIMMABLE also contains #minecraft:water, therefore vanilla water
     * continues to behave normally.
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
     *
     * Vanilla LivingEntity.baseTick() checks:
     *
     *     isEyeInFluid(FluidTags.WATER)
     *
     * before reducing air supply.
     *
     * Forge's FluidType system extends this behavior to custom fluids
     * whose FluidType allows drowning. Mechanical Botany's Compost
     * fluids use that default drowning behavior.
     *
     * Fabric does not automatically bridge FluidType.canDrown() into
     * vanilla's breathing system, so we substitute our SWIMMABLE tag.
     *
     * Because SWIMMABLE contains both vanilla water and our three
     * placeable Compost fluids, this preserves normal water drowning
     * while adding:
     *
     * - Liquid Compost
     * - Molten Liquid Compost
     * - Void Liquid Compost
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