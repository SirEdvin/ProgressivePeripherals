package site.siredvin.progressiveperipherals.extra.network.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.extra.network.GlobalNetworksData;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireNetworkComponent;
import site.siredvin.progressiveperipherals.extra.network.api.IEnderwireElement;
import site.siredvin.progressiveperipherals.utils.ExtraLuaConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnderwireNetworkProducer {

    public static void firePoweredEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @NotNull EnderwireNetworkComponent component, @Nullable PlayerEntity player, boolean extendedData) {
        if (!world.isClientSide) {
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null) {
                String attachedNetwork = te.getAttachedNetwork();
                GlobalNetworksData networksData = GlobalNetworksData.get((ServerWorld) world);
                NetworkData currentNetwork = networksData.getNetwork(attachedNetwork);
                if (attachedNetwork != null && currentNetwork != null) {
                    String eventName = component.getEnableEventName();
                    if (!isEnabled)
                        eventName = component.getDisableEventName();
                    Map<String, Object> data = new HashMap<>();
                    data.put("element", te.getElementUUID().toString());
                    if (extendedData && player != null)
                        data.put("player", player.getName().getString());
                    EnderwireNetworkBusHub.fireComputerEvent(attachedNetwork, EnderwireComputerEvent.timed(
                            eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                            world.dimension().location().toString(), pos, data
                    ));
                }
            }
        }
    }

    public static void firePoweredLeverEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @Nullable PlayerEntity player, boolean extendedData) {
        firePoweredEvent(isEnabled, world, pos, EnderwireNetworkComponent.LEVER, player, extendedData);
    }

    public static void firePoweredButtonEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @Nullable PlayerEntity player, boolean extendedData) {
        firePoweredEvent(isEnabled, world, pos, EnderwireNetworkComponent.BUTTON, player, extendedData);
    }

    public static void firePoweredPlateEvent(boolean isEnabled, @NotNull World world, @NotNull BlockPos pos, @Nullable List<? extends Entity> collidingEntities, boolean extendedData) {
        if (!world.isClientSide) {
            IEnderwireElement<?> te = (IEnderwireElement<?>) world.getBlockEntity(pos);
            if (te != null) {
                String attachedNetwork = te.getAttachedNetwork();
                GlobalNetworksData networksData = GlobalNetworksData.get((ServerWorld) world);
                NetworkData currentNetwork = networksData.getNetwork(attachedNetwork);
                if (attachedNetwork != null && currentNetwork != null) {
                    String eventName = EnderwireNetworkComponent.PLATE.getEnableEventName();
                    if (!isEnabled)
                        eventName = EnderwireNetworkComponent.PLATE.getDisableEventName();
                    Map<String, Object> data = new HashMap<>();
                    data.put("element", te.getElementUUID().toString());
                    if (extendedData && collidingEntities != null)
                        data.put("entities", collidingEntities.stream().map(ExtraLuaConverter::classifyEntity).collect(Collectors.toList()));
                    EnderwireNetworkBusHub.fireComputerEvent(attachedNetwork, EnderwireComputerEvent.timed(
                            eventName, currentNetwork.getReachableRange(), currentNetwork.isInterdimensional(),
                            world.dimension().location().toString(), pos, data
                    ));
                }
            }
        }
    }
}