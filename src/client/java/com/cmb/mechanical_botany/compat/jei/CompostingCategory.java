package com.cmb.mechanical_botany.compat.jei;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.recipe.CompostingRecipe;
import com.cmb.mechanical_botany.registry.ModBlocks;
import com.cmb.mechanical_botany.registry.ModItems;
import com.cmb.mechanical_botany.registry.ModRecipeTypes;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CompostingCategory
        extends CreateRecipeCategory<CompostingRecipe> {

    /*
     * ================================================================
     * JEI RECIPE TYPE
     * ================================================================
     */

    public static final RecipeType<CompostingRecipe> TYPE =
            RecipeType.create(
                    MechanicalBotany.MOD_ID,
                    "composting",
                    CompostingRecipe.class
            );

    /*
     * ================================================================
     * ANIMATED MACHINE
     * ================================================================
     */

    private final AnimatedMechanicalComposter mechanicalComposter =
            new AnimatedMechanicalComposter();

    public CompostingCategory(
            Info<CompostingRecipe> info
    ) {
        super(
                info
        );
    }

    /*
     * ================================================================
     * RECIPE LAYOUT
     * ================================================================
     */

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            CompostingRecipe recipe,
            IFocusGroup focuses
    ) {

        /*
         * ============================================================
         * INPUT
         * ============================================================
         */

        builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        5,
                        9
                )
                .setBackground(
                        getRenderedSlot(),
                        -1,
                        -1
                )
                .addIngredients(
                        recipe.getIngredients()
                                .get(0)
                );

        /*
         * ============================================================
         * OUTPUTS
         * ============================================================
         */

        List<ProcessingOutput> results =
                recipe.getRollableResults();

        boolean singleResult =
                results.size() == 1;

        for (
                int index = 0;
                index < results.size();
                index++
        ) {
            ProcessingOutput output =
                    results.get(
                            index
                    );

            int xOffset =
                    index % 2 == 0
                            ? 0
                            : 19;

            int yOffset =
                    index / 2 * -19;

            builder.addSlot(
                            RecipeIngredientRole.OUTPUT,
                            singleResult
                                    ? 139
                                    : 133 + xOffset,
                            37 + yOffset
                    )
                    .setBackground(
                            getRenderedSlot(
                                    output
                            ),
                            -1,
                            -1
                    )
                    .addItemStack(
                            output.getStack()
                    )
                    .addRichTooltipCallback(
                            addStochasticTooltip(
                                    output
                            )
                    );
        }
    }

    /*
     * ================================================================
     * CATEGORY CREATION
     * ================================================================
     */

    public static CompostingCategory create(
            IGuiHelper guiHelper
    ) {

        Component title =
                Component.translatable(
                        "recipe.mechanical_botany.composting"
                );

        EmptyBackground background =
                new EmptyBackground(
                        178,
                        72
                );

        /*
         * Original 1.0.5 uses a double icon consisting of:
         *
         * Mechanical Composter + Compost
         */

        DoubleItemIcon icon =
                new DoubleItemIcon(
                        () ->
                                new ItemStack(
                                        ModBlocks.MECHANICAL_COMPOSTER
                                ),
                        () ->
                                new ItemStack(
                                        ModItems.COMPOST
                                )
                );

        Info<CompostingRecipe> info =
                new Info<>(
                        TYPE,
                        title,
                        background,
                        icon,
                        CompostingCategory::getAllRecipes,
                        List.of(
                                () ->
                                        new ItemStack(
                                                ModBlocks.MECHANICAL_COMPOSTER
                                        )
                        )
                );

        return new CompostingCategory(
                info
        );
    }

    /*
     * ================================================================
     * RECIPE COLLECTION
     * ================================================================
     */

    private static List<CompostingRecipe> getAllRecipes() {
        return CreateMechanicalBotanyJEI
                .getRecipeManager()
                .getAllRecipesFor(
                        ModRecipeTypes.COMPOSTING_TYPE
                );
    }

    /*
     * ================================================================
     * BACKGROUND DRAWING
     * ================================================================
     */

    @Override
    public void draw(
            CompostingRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics graphics,
            double mouseX,
            double mouseY
    ) {

        /*
         * Output arrow.
         */

        AllGuiTextures.JEI_ARROW.render(
                graphics,
                85,
                41
        );

        /*
         * Input arrow.
         */

        AllGuiTextures.JEI_DOWN_ARROW.render(
                graphics,
                45,
                20
        );

        /*
         * Animated Mechanical Composter.
         */

        mechanicalComposter.draw(
                graphics,
                38,
                27
        );
    }
}