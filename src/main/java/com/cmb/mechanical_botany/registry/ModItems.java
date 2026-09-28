package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {

    /*
     * ================================================================
     * BLOCK ITEMS
     * ================================================================
     */

    public static final Item MECHANICAL_INSOLATOR =
            new BlockItem(
                    ModBlocks.MECHANICAL_INSOLATOR,
                    new Item.Properties()
            );

    public static final Item MECHANICAL_COMPOSTER =
            new BlockItem(
                    ModBlocks.MECHANICAL_COMPOSTER,
                    new Item.Properties()
            );

    /*
     * ================================================================
     * NORMAL ITEMS
     * ================================================================
     */

    public static final Item COMPOST =
            new Item(
                    new Item.Properties()
            );

    private ModItems() {
    }

    /*
     * ================================================================
     * REGISTRATION
     * ================================================================
     */

    public static void register() {

        /*
         * ============================================================
         * BLOCK ITEMS
         * ============================================================
         */

        Registry.register(
                BuiltInRegistries.ITEM,
                MechanicalBotany.id(
                        "mechanical_insolator"
                ),
                MECHANICAL_INSOLATOR
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                MechanicalBotany.id(
                        "mechanical_composter"
                ),
                MECHANICAL_COMPOSTER
        );

        /*
         * ============================================================
         * NORMAL ITEMS
         * ============================================================
         */

        Registry.register(
                BuiltInRegistries.ITEM,
                MechanicalBotany.id(
                        "compost"
                ),
                COMPOST
        );

        /*
         * ============================================================
         * CREATIVE MODE TAB
         * ============================================================
         *
         * Register the tab after Mechanical Botany's normal Item
         * instances have been registered.
         *
         * The fluid bucket entries themselves are evaluated later by
         * CreativeModeTab's display-items callback, after Registrate has
         * finished registering the Mechanical Botany fluids.
         */

        ModCreativeModeTabs.register();

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany items."
        );
    }
}