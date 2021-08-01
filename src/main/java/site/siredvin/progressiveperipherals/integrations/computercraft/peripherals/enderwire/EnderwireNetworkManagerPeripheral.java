package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals.enderwire;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.pocket.IPocketAccess;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.IPeripheralOperation;
import de.srendi.advancedperipherals.common.addons.computercraft.operations.OperationPeripheral;
import de.srendi.advancedperipherals.common.util.Pair;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkAccessingTool;
import site.siredvin.progressiveperipherals.extra.network.tools.NetworkRepresentationTool;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static site.siredvin.progressiveperipherals.extra.network.tools.NetworkPeripheralTool.withNetworks;

public class EnderwireNetworkManagerPeripheral extends OperationPeripheral {

    public EnderwireNetworkManagerPeripheral(String type, IPocketAccess pocket) {
        super(type, pocket);
    }

    @Override
    public List<IPeripheralOperation<?>> possibleOperations() {
        return Collections.emptyList();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult createPublicNetwork(String name) {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            data.addPublicNetwork(name, player.getUUID());
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult createPrivateNetwork(String name) {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            data.addPrivateNetwork(name, player.getUUID());
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult createEncryptedNetwork(String name, String password) {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork != null)
                return MethodResult.of(null, "This name already taken");
            data.addEncryptedNetwork(name, player.getUUID(), password);
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult removeNetwork(String name) {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            NetworkData existingNetwork = data.getNetwork(name);
            if (existingNetwork == null)
                return MethodResult.of(null, "Cannot find network");
            if (!existingNetwork.getOwnerUUID().equals(player.getUUID()))
                return MethodResult.of(null, "You are not owner of this network");
            boolean removeNetworkResult = data.removeNetwork(name, player.getUUID()) != null;
            if (removeNetworkResult) {
                NetworkData selectedNetwork = NetworkAccessingTool.getSelectedNetwork(data, owner.getDataStorage());
                if (selectedNetwork != null && selectedNetwork.getName().equals(name))
                    NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), null);
            }
            return MethodResult.of(removeNetworkResult);
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult selectNetwork(@NotNull IArguments arguments) throws LuaException {
        String name = arguments.getString(0);
        String password = arguments.optString(1, null);
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            Pair<MethodResult, NetworkData> accessResult = NetworkAccessingTool.accessNetwork(data, name, player, password);
            if (accessResult.leftPresent())
                return accessResult.getLeft();
            NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), accessResult.getRight());
            return MethodResult.of(true);
        });
    }

    @LuaFunction(mainThread = true)
    public final void clearSelectedNetwork() {
        NetworkAccessingTool.writeSelectedNetwork(owner.getDataStorage(), null);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getSelectedNetwork() {
        return withNetworks(getWorld(), owner.getOwner(), (data, player) -> {
            NetworkData network = NetworkAccessingTool.getSelectedNetwork(data, owner.getDataStorage());
            if (network == null)
                return MethodResult.of((Object) null);
            return MethodResult.of(network.getName());
        });
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getAvailableNetworks() {
        return withNetworks(
                getWorld(), owner.getOwner(),
                (data, player) -> MethodResult.of(data.getVisibleNetworks(player.getUUID()).stream().map(NetworkRepresentationTool::shortRepresentation).collect(Collectors.toList())));
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getOwnedNetworks() {
        return withNetworks(
                getWorld(), owner.getOwner(),
                (data, player) -> MethodResult.of(data.getOwnerNetworks(player.getUUID()).stream().map(NetworkRepresentationTool::shortRepresentation).collect(Collectors.toList())));
    }
}