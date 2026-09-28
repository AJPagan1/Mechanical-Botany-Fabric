package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeModeTabs {

    /*
     * ================================================================
     * CREATE: MECHANICAL BOTANY
     * ================================================================
     *
     * Matches the Creative Mode tab from Mechanical Botany 1.0.5.
     *
     * Order:
     *
     * 1. Mechanical Insolator
     * 2. Mechanical Composter
     * 3. Compost
     * 4. Liquid Compost Bucket
     * 5. Molten Liquid Compost Bucket
     * 6. Void Liquid Compost Bucket
     *
     * The Mechanical Insolator is used as the tab icon.
     */

    public static final CreativeModeTab MECHANICAL_BOTANY =
            FabricItemGroup.builder()
                    .title(
                            Component.translatable(
                                    "itemGroup.mechanical_botany.base"
                            )
                    )
                    .icon(
                            () ->
                                    new ItemStack(
                                            ModItems.MECHANICAL_INSOLATOR
                                    )
                    )
                    .displayItems(
                            (
                                    parameters,
                                    output
                            ) -> {

                                /*
                                 * ========================================================
                                 * MACHINES
                                 * ========================================================
                                 */

                                output.accept(
                                        ModItems.MECHANICAL_INSOLATOR
                                );

                                output.accept(
                                        ModItems.MECHANICAL_COMPOSTER
                                );

                                /*
                                 * ========================================================
                                 * ITEMS
                                 * ========================================================
                                 */

                                output.accept(
                                        ModItems.COMPOST
                                );

                                /*
                                 * ========================================================
                                 * FLUID BUCKETS
                                 * ========================================================
                                 */

                                output.accept(
                                        ModFluids.COMPOST
                                                .get()
                                                .getBucket()
                                );

                                output.accept(
                                        ModFluids.MOLTEN_COMPOST
                                                .get()
                                                .getBucket()
                                );

                                output.accept(
                                        ModFluids.VOID_COMPOST
                                                .get()
                                                .getBucket()
                                );
                            }
                    )
                    .build();

    private ModCreativeModeTabs() {
    }

    /*
     * ================================================================
     * REGISTRATION
     * ================================================================
     */

    public static void register() {

        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                MechanicalBotany.id(
                        "mechanical_botany"
                ),
                MECHANICAL_BOTANY
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany creative mode tab."
        );
    }
}