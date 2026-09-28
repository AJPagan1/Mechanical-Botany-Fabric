package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;

public final class ModParticles {

    public static final SimpleParticleType COMPOST_SPLASH =
            FabricParticleTypes.simple();

    public static final SimpleParticleType MOLTEN_COMPOST_SPLASH =
            FabricParticleTypes.simple();

    public static final SimpleParticleType VOID_COMPOST_SPLASH =
            FabricParticleTypes.simple();

    private ModParticles() {
    }

    public static void register() {

        Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                MechanicalBotany.id("compost_splash"),
                COMPOST_SPLASH
        );

        Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                MechanicalBotany.id("molten_compost_splash"),
                MOLTEN_COMPOST_SPLASH
        );

        Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                MechanicalBotany.id("void_compost_splash"),
                VOID_COMPOST_SPLASH
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany particles."
        );
    }
}
