package site.siredvin.progressiveperipherals.common.tileentities.enderwire;

import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireComputerEventType;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireComputerEvent;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireEventSubscription;
import site.siredvin.progressiveperipherals.extra.network.events.EnderwireNetworkBusHub;
import site.siredvin.progressiveperipherals.extra.network.events.IEnderwireEventConsumer;
import site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire.EnderwireNetworkConnectorPeripheral;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnderwireNetworkConnectorTileEntity extends BaseEnderwireTileEntity<EnderwireNetworkConnectorTileEntity, EnderwireNetworkConnectorPeripheral> implements IEnderwireEventConsumer<EnderwireComputerEvent> {

    private EnderwireEventSubscription<EnderwireComputerEvent> subscription;

    public EnderwireNetworkConnectorTileEntity() {
        super(TileEntityTypes.ENDERWIRE_NETWORK_CONNECTOR.get());
    }

    @Override
    public void afterAttachedNetworkChange(String oldNetwork, String newNetwork) {
        if (level != null && !level.isClientSide) {
            if (oldNetwork != null && subscription != null)
                unsubscribeFromEvents(oldNetwork);
            if (newNetwork != null)
                subscribeToEvents(newNetwork);
        }
    }

    @Override
    public void blockTick() {
        super.blockTick();
        if (level != null && !level.isClientSide && attachedNetwork != null && subscription == null) {
            subscribeToEvents();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (attachedNetwork != null)
            unsubscribeFromEvents(attachedNetwork);
    }

    @Override
    public EnderwireNetworkConnectorTileEntity getThis() {
        return this;
    }

    @Override
    public EnderwireElementType getElementType() {
        return EnderwireElementType.NETWORK_MANAGER;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        return new HashMap<>();
    }

    @Override
    protected boolean hasPeripheral() {
        return true;
    }

    @Override
    protected @NotNull EnderwireNetworkConnectorPeripheral createPeripheral() {
        return new EnderwireNetworkConnectorPeripheral(this);
    }

    public void subscribeToEvents(@NotNull String newNetwork) {
        if (level != null && !level.isClientSide && subscription == null) {
            subscription = EnderwireNetworkBusHub.subscribeToComputerEvents(newNetwork, this);
        }
    }

    public void subscribeToEvents() {
        if (level != null && !level.isClientSide && attachedNetwork != null)
            subscribeToEvents(attachedNetwork);
    }

    public void unsubscribeFromEvents(@NotNull String oldNetwork) {
        if (level != null && !level.isClientSide && subscription != null) {
            EnderwireNetworkBusHub.unsubscribeFromComputerEvents(oldNetwork, subscription);
            subscription = null;
        }
    }

    @Override
    public void consume(EnderwireComputerEvent event) {
        if (event.IsNotMalformed(getBlockPos(), Objects.requireNonNull(getWorld()).dimension().location().toString())) {
            getConnectedComputers().forEach(computer -> computer.queueEvent(EnderwireComputerEventType.ENDERWIRE_COMPUTER_EVENT.name().toLowerCase(), event.getData()));
        } else {
            getConnectedComputers().forEach(computer -> computer.queueEvent(EnderwireComputerEventType.MALFORMED_ENDERWIRE_EVENT.name().toLowerCase()));
        }
    }
}
