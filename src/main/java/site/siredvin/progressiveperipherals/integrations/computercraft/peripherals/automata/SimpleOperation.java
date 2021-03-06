package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.automata;

import de.srendi.advancedperipherals.lib.peripherals.IPeripheralOperation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public enum SimpleOperation implements IPeripheralOperation<Object> {
    XP_TRANSFER(1_000, 1),
    ENCHANTMENT(5_000, 10),
    SMITH(1_000, 1),
    BREW(1_000, 5),
    THROW_POTION(1_000, 10),
    FILL_BOTTLES(0, 0),
    CURE(60_000, 50);

    private ForgeConfigSpec.IntValue cooldown;
    private ForgeConfigSpec.IntValue cost;
    private final int defaultCooldown;
    private final int defaultCost;

    SimpleOperation(int defaultCooldown, int defaultCost) {
        this.defaultCooldown = defaultCooldown;
        this.defaultCost = defaultCost;
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        cooldown = builder.defineInRange(
                settingsName() + "Cooldown", defaultCooldown, 0, Integer.MAX_VALUE
        );
        cost = builder.defineInRange(
                settingsName() + "Cost", defaultCost, 0, Integer.MAX_VALUE
        );
    }

    @Override
    public int getInitialCooldown() {
        return cooldown.get();
    }

    @Override
    public int getCooldown(Object context) {
        return cooldown.get();
    }

    @Override
    public int getCost(Object context) {
        return cost.get();
    }

    @Override
    public Map<String, Object> computerDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", settingsName());
        data.put("type", getClass().getSimpleName());
        data.put("cooldown", cooldown.get());
        data.put("cost", cost.get());
        return data;
    }

}
