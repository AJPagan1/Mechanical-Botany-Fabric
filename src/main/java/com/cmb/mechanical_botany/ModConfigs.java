package com.cmb.mechanical_botany;

import com.cmb.mechanical_botany.config.ServerConfig;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ModConfigs {

    private static final Map<ModConfig.Type, ConfigBase> CONFIGS =
            new EnumMap<>(
                    ModConfig.Type.class
            );

    private static ServerConfig server;

    private ModConfigs() {
    }

    public static ServerConfig server() {
        return server;
    }

    public static ConfigBase byType(
            ModConfig.Type type
    ) {
        return CONFIGS.get(
                type
        );
    }

    private static <T extends ConfigBase> T register(
            Supplier<T> factory,
            ModConfig.Type side
    ) {
        Pair<T, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder()
                        .configure(
                                builder -> {
                                    T config =
                                            factory.get();

                                    config.registerAll(
                                            builder
                                    );

                                    return config;
                                }
                        );

        T config =
                specPair.getLeft();

        config.specification =
                specPair.getRight();

        CONFIGS.put(
                side,
                config
        );

        return config;
    }

    public static void register() {
        server =
                register(
                        ServerConfig::new,
                        ModConfig.Type.SERVER
                );

        for (
                Map.Entry<ModConfig.Type, ConfigBase> entry :
                CONFIGS.entrySet()
        ) {
            ForgeConfigRegistry.INSTANCE.register(
                    MechanicalBotany.MOD_ID,
                    entry.getKey(),
                    entry.getValue().specification
            );
        }

        ModConfigEvents
                .loading(
                        MechanicalBotany.MOD_ID
                )
                .register(
                        ModConfigs::onLoad
                );

        ModConfigEvents
                .reloading(
                        MechanicalBotany.MOD_ID
                )
                .register(
                        ModConfigs::onReload
                );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany configuration."
        );
    }

    private static void onLoad(
            ModConfig config
    ) {
        for (
                ConfigBase registeredConfig :
                CONFIGS.values()
        ) {
            if (
                    registeredConfig.specification
                            == config.getSpec()
            ) {
                registeredConfig.onLoad();
            }
        }
    }

    private static void onReload(
            ModConfig config
    ) {
        for (
                ConfigBase registeredConfig :
                CONFIGS.values()
        ) {
            if (
                    registeredConfig.specification
                            == config.getSpec()
            ) {
                registeredConfig.onReload();
            }
        }
    }
}