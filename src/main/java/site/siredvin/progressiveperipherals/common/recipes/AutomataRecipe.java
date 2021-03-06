package site.siredvin.progressiveperipherals.common.recipes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.common.setup.Items;
import site.siredvin.progressiveperipherals.common.setup.RecipeSerializers;

import java.util.Map;
import java.util.Set;

public class AutomataRecipe implements IRecipe<IInventory> {

    public static final int MAX_HEIGHT = 4;
    public static final int MAX_WIDTH = 4;

    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack result;
    private final ResourceLocation id;

    public static final String GROUP = ProgressivePeripherals.MOD_ID + ":automata";

    public AutomataRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, ItemStack result) {
        this.id = id;
        this.recipeItems = recipeItems;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory inventory, @NotNull World world) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if(!recipeItems.get(i).test(inventory.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    public void craft(IInventory inventory) {
        // remove items from inventory
        int firstEmptySlot = - 1;
        for (int i = 0; i< inventory.getContainerSize(); i++) {
            ItemStack currentItem = inventory.getItem(i);
            if (currentItem.isEmpty()) {
                if (firstEmptySlot == -1)
                    firstEmptySlot = i;
                continue;
            }
            if (currentItem.getCount() > 1) {
                currentItem.setCount(currentItem.getCount() - 1);
                inventory.setItem(i, currentItem);
            } else {
                inventory.setItem(i, ItemStack.EMPTY);
                if (firstEmptySlot == -1)
                    firstEmptySlot = i;
            }
        }
        if (firstEmptySlot == -1)
            throw new IllegalArgumentException("This is very bad, where I should put crafting result?");
        inventory.setItem(firstEmptySlot, result.copy());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull IInventory inventory) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 4 && height == 4;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull IRecipeSerializer<?> getSerializer()
    {
        return RecipeSerializers.AUTOMATA_CRAFTING.get();
    }

    @Override
    public @NotNull IRecipeType<?> getType() {
        return RecipeSerializers.AUTOMATA_CRAFTING.get().getRecipeType();
    }

    @Override
    public @NotNull String getGroup() {
        return GROUP;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(Items.SCIENTIFIC_AUTOMATA_CORE.get());
    }

    // Parsing

    protected static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> keyMapping) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(MAX_HEIGHT * MAX_WIDTH, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(keyMapping.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keyMapping.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                ingredients.set(j + MAX_HEIGHT * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return ingredients;
        }
    }

    protected static Map<String, Ingredient> keyFromJson(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    protected static String[] patternFromJson(JsonArray jsonArray) {
        if (jsonArray.size() != MAX_HEIGHT)
            throw new JsonSyntaxException("Invalid pattern: not enough rows, only " + MAX_HEIGHT + " is allowed");
        String[] pattern = new String[jsonArray.size()];
        for(int i = 0; i < pattern.length; ++i) {
            String s = JSONUtils.convertToString(jsonArray.get(i), "pattern[" + i + "]");
            if (s.length() != MAX_WIDTH) {
                throw new JsonSyntaxException("Invalid pattern: not enough columns, only " + MAX_WIDTH + " is allowed");
            }
            pattern[i] = s;
        }

        return pattern;
    }

    public static IRecipeType<AutomataRecipe> TYPE() {
        return RecipeSerializers.AUTOMATA_CRAFTING.get().getRecipeType();
    }
}
