package site.siredvin.progressiveperipherals.extra.recipes;

import net.minecraft.item.crafting.IRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RecipeTransformer<T extends IRecipe<?>> implements IRecipeTransformer<T> {

    public abstract List<?> getInputs(T recipe);

    public abstract List<?> getOutputs(T recipe);

    public @Nullable Map<String, Object> getExtraData(T recipe) {
        return null;
    }

    protected List<Object> serializeIngredients(List<?> originalData) {
        return originalData.stream().filter(Objects::nonNull).map(RecipeRegistryToolkit::serialize)
                .filter(obj -> obj != RecipeRegistryToolkit.SERIALIZATION_NULL).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> transform(T recipe) {
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("id", recipe.getId().toString());
        recipeData.put("type", recipe.getType().toString());
        recipeData.put("output", serializeIngredients(getOutputs(recipe)));
        recipeData.put("input", serializeIngredients(getInputs(recipe)));

        Map<String, Object> extraData = getExtraData(recipe);
        if (extraData != null) {
            // extra cleanup
            List<String> cleanupList = new ArrayList<>();
            for (String key: extraData.keySet()) {
                if (extraData.get(key) == RecipeRegistryToolkit.SERIALIZATION_NULL)
                    cleanupList.add(key);
            }
            cleanupList.forEach(extraData::remove);
            recipeData.put("extra", extraData);
        }

        return recipeData;
    }
}
