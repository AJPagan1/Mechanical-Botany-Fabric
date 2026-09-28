package com.cmb.mechanical_botany.registry;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.recipe.CompostingRecipe;
import com.cmb.mechanical_botany.recipe.InsolatingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class ModRecipeTypes {

    /*
     * ================================================================
     * COMPOSTING
     * ================================================================
     */

    public static final ResourceLocation COMPOSTING_ID =
            MechanicalBotany.id("composting");

    public static final RecipeType<CompostingRecipe> COMPOSTING_TYPE =
            new RecipeType<>() {

                @Override
                public String toString() {
                    return COMPOSTING_ID.toString();
                }
            };

    public static final RecipeSerializer<CompostingRecipe>
            COMPOSTING_SERIALIZER =
            new ProcessingRecipeSerializer<>(
                    CompostingRecipe::new
            );

    public static final IRecipeTypeInfo COMPOSTING =
            new IRecipeTypeInfo() {

                @Override
                public ResourceLocation getId() {
                    return COMPOSTING_ID;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends RecipeSerializer<?>> T getSerializer() {
                    return (T) COMPOSTING_SERIALIZER;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends RecipeType<?>> T getType() {
                    return (T) COMPOSTING_TYPE;
                }
            };

    /*
     * ================================================================
     * INSOLATING
     * ================================================================
     */

    public static final ResourceLocation INSOLATING_ID =
            MechanicalBotany.id("insolating");

    public static final RecipeType<InsolatingRecipe> INSOLATING_TYPE =
            new RecipeType<>() {

                @Override
                public String toString() {
                    return INSOLATING_ID.toString();
                }
            };

    public static final RecipeSerializer<InsolatingRecipe>
            INSOLATING_SERIALIZER =
            new ProcessingRecipeSerializer<>(
                    InsolatingRecipe::new
            );

    public static final IRecipeTypeInfo INSOLATING =
            new IRecipeTypeInfo() {

                @Override
                public ResourceLocation getId() {
                    return INSOLATING_ID;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends RecipeSerializer<?>> T getSerializer() {
                    return (T) INSOLATING_SERIALIZER;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends RecipeType<?>> T getType() {
                    return (T) INSOLATING_TYPE;
                }
            };

    private ModRecipeTypes() {
    }

    public static void register() {

        /*
         * Composting
         */
        Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                COMPOSTING_ID,
                COMPOSTING_TYPE
        );

        Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                COMPOSTING_ID,
                COMPOSTING_SERIALIZER
        );

        /*
         * Insolating
         */
        Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                INSOLATING_ID,
                INSOLATING_TYPE
        );

        Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                INSOLATING_ID,
                INSOLATING_SERIALIZER
        );

        MechanicalBotany.LOGGER.info(
                "Registered Mechanical Botany recipe types."
        );
    }
}