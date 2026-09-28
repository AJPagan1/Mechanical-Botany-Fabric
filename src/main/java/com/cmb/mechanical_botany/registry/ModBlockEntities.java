package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.kinetics.composter.MechanicalComposterBlockEntity;
import com.cmb.mechanical_botany.kinetics.insolator.MechanicalInsolatorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {

    public static final BlockEntityType<MechanicalComposterBlockEntity>
            MECHANICAL_COMPOSTER =
            FabricBlockEntityTypeBuilder.create(
                    MechanicalComposterBlockEntity::new,
                    ModBlocks.MECHANICAL_COMPOSTER
            ).build();

    public static final BlockEntityType<MechanicalInsolatorBlockEntity>
            MECHANICAL_INSOLATOR =
            FabricBlockEntityTypeBuilder.create(
                    MechanicalInsolatorBlockEntity::new,
                    ModBlocks.MECHANICAL_INSOLATOR
            ).build();

    private ModBlockEntities() {
    }

    public static void register() {
        Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                MechanicalBotany.id("mechanical_composter"),
                MECHANICAL_COMPOSTER
        );

        Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                MechanicalBotany.id("mechanical_insolator"),
                MECHANICAL_INSOLATOR
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany block entities."
        );
    }
}