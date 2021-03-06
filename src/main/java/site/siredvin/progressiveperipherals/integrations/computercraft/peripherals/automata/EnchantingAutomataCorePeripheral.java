package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.lib.peripherals.IPeripheralOperation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.PPAbilities;
import site.siredvin.progressiveperipherals.utils.CheckUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata.SimpleOperation.ENCHANTMENT;

public class EnchantingAutomataCorePeripheral extends ExperienceAutomataCorePeripheral {

    public static final String TYPE = "enchantingAutomataCore";

    private static final int MINECRAFT_ENCHANTING_LEVEL_LIMIT = 30;

    public EnchantingAutomataCorePeripheral(ITurtleAccess turtle, TurtleSide side) {
        super(TYPE, turtle, side, AutomataCoreTier.TIER3);
    }

    @Override
    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = super.getPeripheralConfiguration();
        data.put("enchantLevelCost", ProgressivePeripheralsConfig.enchantLevelCost);
        data.put("treasureEnchantmentsAllowed", allowTreasureEnchants());
        data.put("enchantmentWipeChance", ProgressivePeripheralsConfig.enchantingAutomataCoreDisappearChance);
        return data;
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        List<IPeripheralOperation<?>> data = super.possibleOperations();
        data.add(ENCHANTMENT);
        return data;
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableEnchantingAutomataCore;
    }

    @SuppressWarnings("SameReturnValue")
    public boolean allowTreasureEnchants() {
        return false;
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult enchant(int levels) throws LuaException {
        return withOperation(ENCHANTMENT, context -> {
            if (levels > MINECRAFT_ENCHANTING_LEVEL_LIMIT)
                return MethodResult.of(null, String.format("Enchanting levels cannot be bigger then %d", MINECRAFT_ENCHANTING_LEVEL_LIMIT));
            ExperienceAbility experienceAbility = owner.getAbility(PPAbilities.EXPERIENCE);
            if (experienceAbility == null)
                return MethodResult.of(null, "Internal error ...?");
            addRotationCycle();
            int requiredXP = levels * ProgressivePeripheralsConfig.enchantLevelCost;
            if (requiredXP > experienceAbility._getStoredXP())
                return MethodResult.of(null, String.format("Not enough XP, %d required", requiredXP));
            int selectedSlot = owner.turtle.getSelectedSlot();
            IInventory turtleInventory = owner.turtle.getInventory();
            ItemStack targetItem = turtleInventory.getItem(selectedSlot);
            if (!targetItem.isEnchantable())
                return MethodResult.of(null, "Item is not enchantable");
            if (targetItem.isEnchanted())
                return MethodResult.of(null, "Item already enchanted!");
            ItemStack enchantedItem = EnchantmentHelper.enchantItem(getWorld().random, owner.getToolInMainHand(), levels, allowTreasureEnchants());
            experienceAbility.adjustStoredXP(-requiredXP);
            turtleInventory.setItem(selectedSlot, enchantedItem);
            return MethodResult.of(true);
        });
    }

    @SuppressWarnings("unused")
    @LuaFunction(mainThread = true)
    public final MethodResult extractEnchantment(int target) throws LuaException {
        CheckUtils.isCorrectSlot(target);
        int realSlot = target - 1;
        return withOperation(ENCHANTMENT, context -> {
            IInventory turtleInventory = owner.turtle.getInventory();
            int selectedSlot = owner.turtle.getSelectedSlot();
            ItemStack selectedItem = turtleInventory.getItem(selectedSlot);
            ItemStack targetItem = turtleInventory.getItem(realSlot);
            if (!selectedItem.isEnchanted())
                return MethodResult.of(null, "Selected item is not enchanted");
            if (!targetItem.getItem().equals(Items.BOOK))
                return MethodResult.of(null, "Target item is not book");
            if (targetItem.getCount() != 1)
                return MethodResult.of(null, "Target book should be 1 in stack");
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(selectedItem);
            if (getWorld().random.nextInt(100) < ProgressivePeripheralsConfig.enchantingAutomataCoreDisappearChance * 100) {
                enchants.keySet().stream().findAny().ifPresent(enchants::remove);
            }
            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(enchants, enchantedBook);
            EnchantmentHelper.setEnchantments(Collections.emptyMap(), selectedItem);
            turtleInventory.setItem(realSlot, enchantedBook);
            return MethodResult.of(true);
        });
    }
}
