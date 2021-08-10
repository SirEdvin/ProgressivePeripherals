package site.siredvin.progressiveperipherals.extra.network.api;

import de.srendi.advancedperipherals.common.addons.computercraft.base.IConfigHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;

public enum EnderwireElementType implements IConfigHandler {
    SENSOR_MANAGER(EnderwireElementCategory.SERVICE), PERIPHERAL_CONNECTOR(EnderwireElementCategory.SERVICE), NETWORK_CONNECTOR(EnderwireElementCategory.SERVICE),
    NETWORK_AMPLIFIER(EnderwireElementCategory.MODIFICATION), DIMENSION_BREAKER(EnderwireElementCategory.MODIFICATION),
    LEVER(EnderwireElementCategory.SENSOR), BUTTON(EnderwireElementCategory.SENSOR), PLATE(EnderwireElementCategory.SENSOR), REDSTONE_SENSOR(EnderwireElementCategory.SENSOR),
    REDSTONE_EMITTER(EnderwireElementCategory.EMITTER), LIGHT_EMITTER(EnderwireElementCategory.EMITTER);

    private final EnderwireElementCategory category;
    private ForgeConfigSpec.BooleanValue enabled;

    EnderwireElementType(EnderwireElementCategory category) {
        this.category = category;
    }

    public EnderwireElementCategory getCategory() {
        return category;
    }

    public String getDisabledEventName() {
        return name().toLowerCase() + "_disabled";
    }

    public String getEnabledEventName() {
        return name().toLowerCase() + "_enabled";
    }

    public String getChangedEventName() {
        return name().toLowerCase() + "_changed";
    }

    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enderwireNetworkEnabled && enabled.get();
    }

    @Override
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        enabled = builder.define(settingsName() + "Enabled", true);
    }

    @Override
    public String settingsPostfix() {
        return "Enderwire";
    }
}
