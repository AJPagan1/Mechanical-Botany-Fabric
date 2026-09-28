package com.cmb.mechanical_botany.recipe;

import com.cmb.mechanical_botany.ModConfigs;
import com.cmb.mechanical_botany.registry.ModRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CompostingRecipe
        extends AbstractCrushingRecipe {

    public CompostingRecipe(
            ProcessingRecipeBuilder.ProcessingRecipeParams params
    ) {
        super(
                ModRecipeTypes.COMPOSTING,
                params
        );
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

        ItemStack inputStack =
                container.getItem(0);

        if (inputStack.isEmpty()) {
            return false;
        }

        return ingredients
                .get(0)
                .test(
                        inputStack
                );
    }

    @Override
    public int getProcessingDuration() {
        return super.getProcessingDuration()
                * ModConfigs
                .server()
                .composter
                .processingTimeMultiplier
                .get();
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
}