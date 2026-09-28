package com.cmb.mechanical_botany.recipe;

import com.cmb.mechanical_botany.ModConfigs;
import com.cmb.mechanical_botany.registry.ModRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class InsolatingRecipe
        extends ProcessingRecipe<Container> {

    public InsolatingRecipe(
            ProcessingRecipeBuilder.ProcessingRecipeParams params
    ) {
        super(
                ModRecipeTypes.INSOLATING,
                params
        );
    }

    public FluidIngredient getRequiredFluid() {
        return fluidIngredients.get(0);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    public int getProcessingDuration() {
        return super.getProcessingDuration()
                * ModConfigs
                .server()
                .insolator
                .processingTimeMultiplier
                .get();
    }

    @Override
    public boolean matches(
            Container container,
            Level level
    ) {
        if (container.isEmpty()) {
            return false;
        }

        if (ingredients.isEmpty()) {
            return false;
        }

        if (fluidIngredients.isEmpty()) {
            return false;
        }

        ItemStack inputStack =
                container.getItem(0);

        if (inputStack.isEmpty()) {
            return false;
        }

        if (
                !ingredients
                        .get(0)
                        .test(
                                inputStack
                        )
        ) {
            return false;
        }

        if (
                getRequiredFluid()
                        .getMatchingFluidStacks()
                        .isEmpty()
        ) {
            return false;
        }

        return getRequiredFluid().test(
                getRequiredFluid()
                        .getMatchingFluidStacks()
                        .get(0)
        );
    }
}