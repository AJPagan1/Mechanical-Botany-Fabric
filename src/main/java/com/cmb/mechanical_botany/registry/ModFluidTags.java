package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public final class ModFluidTags {

    public static final TagKey<Fluid> SWIMMABLE =
            TagKey.create(
                    Registries.FLUID,
                    MechanicalBotany.id("swimmable")
            );

    private ModFluidTags() {
    }
}