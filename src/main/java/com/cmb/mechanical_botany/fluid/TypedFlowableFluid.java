package com.cmb.mechanical_botany.fluid;

import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import io.github.fabricators_of_create.porting_lib.fluids.FluidType;
import io.github.fabricators_of_create.porting_lib.fluids.extensions.FluidExtension;

import java.util.function.Supplier;

public final class TypedFlowableFluid {

    private TypedFlowableFluid() {
    }

    public static class Flowing
            extends SimpleFlowableFluid.Flowing
            implements FluidExtension {

        private final Supplier<FluidType> fluidType;

        public Flowing(
                Properties properties,
                Supplier<FluidType> fluidType
        ) {
            super(properties);

            this.fluidType =
                    fluidType;
        }

        @Override
        public FluidType getFluidType() {
            return fluidType.get();
        }
    }

    public static class Source
            extends SimpleFlowableFluid.Source
            implements FluidExtension {

        private final Supplier<FluidType> fluidType;

        public Source(
                Properties properties,
                Supplier<FluidType> fluidType
        ) {
            super(properties);

            this.fluidType =
                    fluidType;
        }

        @Override
        public FluidType getFluidType() {
            return fluidType.get();
        }
    }
}