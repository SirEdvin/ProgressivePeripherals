package site.siredvin.progressiveperipherals.integrations.jei.automata;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.recipes.AutomataRecipe;
import site.siredvin.progressiveperipherals.common.setup.Items;

import java.util.List;

public class AutomataRecipeCategory implements IRecipeCategory<AutomataRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ProgressivePeripherals.MOD_ID, "textures/gui/jei/jei_automata.png");
    public static final ResourceLocation UID = new ResourceLocation(AutomataRecipe.GROUP);

    public AutomataRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(new ItemStack(Items.SCIENTIFIC_AUTOMATA_CORE.get()));
        background = guiHelper.createDrawable(GUI_TEXTURE, 0, 0, 132, 75);
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }

    @Override
    public @NotNull Class<? extends AutomataRecipe> getRecipeClass() {
        return AutomataRecipe.class;
    }

    @Override
    public @NotNull String getTitle() {
        return new TranslationTextComponent("jei.category.progressiveperipherals.automata").getString();
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(AutomataRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull AutomataRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

        guiItemStacks.init(0, false, 110, 27);
        guiItemStacks.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int index = j + (i * 4);
                guiItemStacks.init(index + 1, true, j * 18, i * 18);
                guiItemStacks.set(index + 1, inputs.get(index));
            }
        }
    }
}
