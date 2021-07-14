package site.siredvin.ancientperipherals.common.setup;

import de.srendi.advancedperipherals.common.setup.Villagers;
import site.siredvin.ancientperipherals.common.items.ForgedAutomataCore;

public class FeedingRecipes {
    public static void register() {
        ForgedAutomataCore.addForgedSoulRecipe(Villagers.COMPUTER_SCIENTIST.get(), Items.SCIENTIFIC_AUTOMATA_CORE.get());
    }
}