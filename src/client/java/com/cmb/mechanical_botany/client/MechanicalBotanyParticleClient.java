package com.cmb.mechanical_botany.client;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.registry.ModParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SplashParticle;

public final class MechanicalBotanyParticleClient {

    private MechanicalBotanyParticleClient() {
    }

    public static void register() {

        ParticleFactoryRegistry
                .getInstance()
                .register(
                        ModParticles.COMPOST_SPLASH,
                        SplashParticle.Provider::new
                );

        ParticleFactoryRegistry
                .getInstance()
                .register(
                        ModParticles.MOLTEN_COMPOST_SPLASH,
                        SplashParticle.Provider::new
                );

        ParticleFactoryRegistry
                .getInstance()
                .register(
                        ModParticles.VOID_COMPOST_SPLASH,
                        SplashParticle.Provider::new
                );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany particle factories."
        );
    }
}