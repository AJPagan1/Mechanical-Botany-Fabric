package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.fluid.TypedFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.fabricators_of_create.porting_lib.fluids.FluidType;
import io.github.fabricators_of_create.porting_lib.fluids.PortingLibFluids;
import net.minecraft.core.Registry;

public final class ModFluids {

    /*
     * ================================================================
     * FLUID TYPES
     * ================================================================
     *
     * Exact Mechanical Botany 1.0.5 values:
     *
     * Compost fluids:
     *   density   = 1400
     *   viscosity = 2000
     */

    public static final FluidType COMPOST_FLUID_TYPE =
            new FluidType(
                    FluidType.Properties
                            .create()
                            .density(1400)
                            .viscosity(2000)
            );

    public static final FluidType MOLTEN_COMPOST_FLUID_TYPE =
            new FluidType(
                    FluidType.Properties
                            .create()
                            .density(1400)
                            .viscosity(2000)
            );

    public static final FluidType VOID_COMPOST_FLUID_TYPE =
            new FluidType(
                    FluidType.Properties
                            .create()
                            .density(1400)
                            .viscosity(2000)
            );

    /*
     * ================================================================
     * LIQUID COMPOST
     * ================================================================
     */

    public static final FluidEntry<TypedFlowableFluid.Flowing> COMPOST =
            MechanicalBotany.REGISTRATE
                    .fluid(
                            "compost",
                            MechanicalBotany.id(
                                    "fluid/compost_still"
                            ),
                            MechanicalBotany.id(
                                    "fluid/compost_flow"
                            ),
                            properties ->
                                    new TypedFlowableFluid.Flowing(
                                            properties,
                                            () -> COMPOST_FLUID_TYPE
                                    )
                    )
                    .lang(
                            "Liquid Compost"
                    )
                    .fluidProperties(
                            properties ->
                                    properties
                                            .levelDecreasePerBlock(2)
                                            .tickRate(25)
                                            .flowSpeed(3)
                                            .blastResistance(100.0F)
                    )
                    .source(
                            properties ->
                                    new TypedFlowableFluid.Source(
                                            properties,
                                            () -> COMPOST_FLUID_TYPE
                                    )
                    )
                    .block()
                    .build()
                    .bucket()
                    .build()
                    .register();

    /*
     * ================================================================
     * MOLTEN COMPOST
     * ================================================================
     */

    public static final FluidEntry<TypedFlowableFluid.Flowing>
            MOLTEN_COMPOST =
            MechanicalBotany.REGISTRATE
                    .fluid(
                            "molten_compost",
                            MechanicalBotany.id(
                                    "fluid/molten_compost_still"
                            ),
                            MechanicalBotany.id(
                                    "fluid/molten_compost_flow"
                            ),
                            properties ->
                                    new TypedFlowableFluid.Flowing(
                                            properties,
                                            () -> MOLTEN_COMPOST_FLUID_TYPE
                                    )
                    )
                    .lang(
                            "Molten Liquid Compost"
                    )
                    .fluidProperties(
                            properties ->
                                    properties
                                            .levelDecreasePerBlock(2)
                                            .tickRate(25)
                                            .flowSpeed(3)
                                            .blastResistance(100.0F)
                    )
                    .source(
                            properties ->
                                    new TypedFlowableFluid.Source(
                                            properties,
                                            () -> MOLTEN_COMPOST_FLUID_TYPE
                                    )
                    )
                    .block()
                    .build()
                    .bucket()
                    .build()
                    .register();

    /*
     * ================================================================
     * VOID COMPOST
     * ================================================================
     */

    public static final FluidEntry<TypedFlowableFluid.Flowing>
            VOID_COMPOST =
            MechanicalBotany.REGISTRATE
                    .fluid(
                            "void_compost",
                            MechanicalBotany.id(
                                    "fluid/void_compost_still"
                            ),
                            MechanicalBotany.id(
                                    "fluid/void_compost_flow"
                            ),
                            properties ->
                                    new TypedFlowableFluid.Flowing(
                                            properties,
                                            () -> VOID_COMPOST_FLUID_TYPE
                                    )
                    )
                    .lang(
                            "Void Liquid Compost"
                    )
                    .fluidProperties(
                            properties ->
                                    properties
                                            .levelDecreasePerBlock(2)
                                            .tickRate(25)
                                            .flowSpeed(3)
                                            .blastResistance(100.0F)
                    )
                    .source(
                            properties ->
                                    new TypedFlowableFluid.Source(
                                            properties,
                                            () -> VOID_COMPOST_FLUID_TYPE
                                    )
                    )
                    .block()
                    .build()
                    .bucket()
                    .build()
                    .register();

    private ModFluids() {
    }

    public static void register() {

        /*
         * Porting Lib's FluidType registry is the Fabric equivalent of
         * NeoForge's FluidType registry.
         */

        Registry.register(
                PortingLibFluids.FLUID_TYPES,
                MechanicalBotany.id(
                        "compost"
                ),
                COMPOST_FLUID_TYPE
        );

        Registry.register(
                PortingLibFluids.FLUID_TYPES,
                MechanicalBotany.id(
                        "molten_compost"
                ),
                MOLTEN_COMPOST_FLUID_TYPE
        );

        Registry.register(
                PortingLibFluids.FLUID_TYPES,
                MechanicalBotany.id(
                        "void_compost"
                ),
                VOID_COMPOST_FLUID_TYPE
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany fluid types."
        );
    }
}