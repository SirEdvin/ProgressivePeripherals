package site.siredvin.progressiveperipherals.data.builders;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

import java.util.Objects;

public class SmithingRecipe implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Ingredient base;
    private final Ingredient addition;
    private final Item result;

    private SmithingRecipe(ResourceLocation id, Ingredient base, Ingredient addition, Item result) {
        this.id = id;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public void serializeRecipeData(JsonObject p_218610_1_) {
        p_218610_1_.add("base", this.base.toJson());
        p_218610_1_.add("addition", this.addition.toJson());
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
        p_218610_1_.add("result", jsonobject);
    }

    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull IRecipeSerializer<?> getType() {
        return IRecipeSerializer.SMITHING;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }

    public static IFinishedRecipe of(Ingredient base, Ingredient addition, Item result, ResourceLocation location) {
        return new SmithingRecipe(location, base, addition, result);
    }

    public static IFinishedRecipe of(Ingredient base, Ingredient addition, Item result) {
        return of(base, addition, result, new ResourceLocation(ProgressivePeripherals.MOD_ID, Objects.requireNonNull(result.getRegistryName()).getPath()));
    }

    public static IFinishedRecipe of(Ingredient base, Ingredient addition, Item result, String prefix) {
        return of(base, addition, result, new ResourceLocation(ProgressivePeripherals.MOD_ID, Objects.requireNonNull(result.getRegistryName()).getPath() + "_" + prefix));
    }
}
