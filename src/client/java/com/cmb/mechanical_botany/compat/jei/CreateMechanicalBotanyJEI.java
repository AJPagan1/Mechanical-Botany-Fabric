package com.cmb.mechanical_botany.compat.jei;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class CreateMechanicalBotanyJEI
        implements IModPlugin {

    /*
     * ================================================================
     * PLUGIN ID
     * ================================================================
     */

    public static final ResourceLocation PLUGIN_UID =
            MechanicalBotany.id(
                    "jei_plugin"
            );

    /*
     * ================================================================
     * CATEGORIES
     * ================================================================
     */

    private final List<IRecipeCategory<?>> categories =
            new ArrayList<>();

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    /*
     * ================================================================
     * CATEGORY REGISTRATION
     * ================================================================
     */

    @Override
    public void registerCategories(
            IRecipeCategoryRegistration registration
    ) {
        categories.clear();

        /*
         * Keep the same ordering as the original 1.0.5 JEI plugin:
         *
         * 1. Insolating
         * 2. Composting
         */

        InsolatingCategory insolatingCategory =
                InsolatingCategory.create(
                        registration
                                .getJeiHelpers()
                                .getGuiHelper()
                );

        CompostingCategory compostingCategory =
                CompostingCategory.create(
                        registration
                                .getJeiHelpers()
                                .getGuiHelper()
                );

        registration.addRecipeCategories(
                insolatingCategory,
                compostingCategory
        );

        categories.add(
                insolatingCategory
        );

        categories.add(
                compostingCategory
        );
    }

    /*
     * ================================================================
     * RECIPE REGISTRATION
     * ================================================================
     */

    @Override
    public void registerRecipes(
            IRecipeRegistration registration
    ) {
        for (
                IRecipeCategory<?> category
                : categories
        ) {
            if (
                    category
                            instanceof CreateRecipeCategory<?> createCategory
            ) {
                createCategory.registerRecipes(
                        registration
                );
            }
        }
    }

    /*
     * ================================================================
     * RECIPE CATALYSTS
     * ================================================================
     */

    @Override
    public void registerRecipeCatalysts(
            IRecipeCatalystRegistration registration
    ) {
        for (
                IRecipeCategory<?> category
                : categories
        ) {
            if (
                    category
                            instanceof CreateRecipeCategory<?> createCategory
            ) {
                createCategory.registerCatalysts(
                        registration
                );
            }
        }
    }

    /*
     * ================================================================
     * CLIENT RECIPE MANAGER
     * ================================================================
     */

    public static RecipeManager getRecipeManager() {
        Minecraft minecraft =
                Minecraft.getInstance();

        if (
                minecraft.level
                        == null
        ) {
            throw new IllegalStateException(
                    "JEI requested Mechanical Botany recipes before a client level was available."
            );
        }

        return minecraft.level
                .getRecipeManager();
    }
}