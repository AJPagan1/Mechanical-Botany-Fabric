package com.cmb.mechanical_botany.compat.jei;

import com.cmb.mechanical_botany.MechanicalBotany;
import com.cmb.mechanical_botany.recipe.InsolatingRecipe;
import com.cmb.mechanical_botany.registry.ModBlocks;
import com.cmb.mechanical_botany.registry.ModRecipeTypes;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class InsolatingCategory
        extends CreateRecipeCategory<InsolatingRecipe> {

    /*
     * ================================================================
     * JEI RECIPE TYPE
     * ================================================================
     */

    public static final RecipeType<InsolatingRecipe> TYPE =
            RecipeType.create(
                    MechanicalBotany.MOD_ID,
                    "insolating",
                    InsolatingRecipe.class
            );

    /*
     * ================================================================
     * ANIMATED MACHINE
     * ================================================================
     */

    private final AnimatedMechanicalInsolator mechanicalInsolator =
            new AnimatedMechanicalInsolator();

    public InsolatingCategory(
            Info<InsolatingRecipe> info
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
            InsolatingRecipe recipe,
            IFocusGroup focuses
    ) {

        /*
         * ============================================================
         * ITEM INPUT
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
         * FLUID INPUT
         * ============================================================
         */

        addFluidSlot(
                builder,
                25,
                9,
                recipe.getRequiredFluid()
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

    public static InsolatingCategory create(
            IGuiHelper guiHelper
    ) {

        Component title =
                Component.translatable(
                        "recipe.mechanical_botany.insolating"
                );

        EmptyBackground background =
                new EmptyBackground(
                        178,
                        72
                );

        ItemIcon icon =
                new ItemIcon(
                        () ->
                                new ItemStack(
                                        ModBlocks.MECHANICAL_INSOLATOR
                                )
                );

        Info<InsolatingRecipe> info =
                new Info<>(
                        TYPE,
                        title,
                        background,
                        icon,
                        InsolatingCategory::getAllRecipes,
                        List.of(
                                () ->
                                        new ItemStack(
                                                ModBlocks.MECHANICAL_INSOLATOR
                                        )
                        )
                );

        return new InsolatingCategory(
                info
        );
    }

    /*
     * ================================================================
     * RECIPE COLLECTION
     * ================================================================
     */

    private static List<InsolatingRecipe> getAllRecipes() {
        return CreateMechanicalBotanyJEI
                .getRecipeManager()
                .getAllRecipesFor(
                        ModRecipeTypes.INSOLATING_TYPE
                );
    }

    /*
     * ================================================================
     * BACKGROUND DRAWING
     * ================================================================
     */

    @Override
    public void draw(
            InsolatingRecipe recipe,
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
         * Animated Mechanical Insolator.
         */

        mechanicalInsolator.draw(
                graphics,
                38,
                27
        );

        /*
         * ============================================================
         * PROCESSING TIME
         * ============================================================
         */

        graphics.drawString(
                Minecraft.getInstance().font,
                Component.translatable(
                        "mechanical_botany.text.processing_ticks",
                        recipe.getProcessingDuration()
                                / 20
                ),
                2,
                60,
                0xFFFFFF
        );
    }
}
