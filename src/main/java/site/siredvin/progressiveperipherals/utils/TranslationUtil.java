package site.siredvin.progressiveperipherals.utils;

import net.minecraft.util.text.TranslationTextComponent;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

public class TranslationUtil {
    public static TranslationTextComponent itemTooltip(String descriptionId) {
        return new TranslationTextComponent(String.format("%s.tooltip", descriptionId));
    }

    public static String turtle(String name) {
        return String.format("turtle.%s.%s", ProgressivePeripherals.MOD_ID, name);
    }

    public static TranslationTextComponent localization(String name) {
        return  new TranslationTextComponent(String.format("text.%s.%s", ProgressivePeripherals.MOD_ID, name));
    }
}