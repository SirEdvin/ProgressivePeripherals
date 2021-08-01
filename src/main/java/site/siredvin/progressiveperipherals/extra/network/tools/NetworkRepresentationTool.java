package site.siredvin.progressiveperipherals.extra.network.tools;

import de.srendi.advancedperipherals.common.util.LuaConverter;
import net.minecraft.util.math.BlockPos;
import site.siredvin.progressiveperipherals.extra.network.NetworkData;
import site.siredvin.progressiveperipherals.extra.network.NetworkElementData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkRepresentationTool {
    public static Map<String, Object> shortRepresentation(NetworkData network) {
        Map<String, Object> representation = new HashMap<>();
        representation.put("name", network.getName());
        representation.put("type", network.getType().name().toLowerCase());
        return representation;
    }

    public static Map<String, Object> fullRepresentation(NetworkData network, BlockPos center) {
        Map<String, Object> representation = shortRepresentation(network);
        Map<UUID, NetworkElementData> elements = network.getElements();
        if (elements != null) {
            Map<String, Object> elementsRepresentation = new HashMap<>();
            elements.values().forEach(networkElement -> elementsRepresentation.put(networkElement.getUUID().toString(), elementRepresentation(networkElement, center)));
            representation.put("elements", elementsRepresentation);
        } else {
            representation.put("elements", null);
        }
        return representation;
    }

    public static Map<String, Object> elementRepresentation(NetworkElementData networkElement, BlockPos center) {
        Map<String, Object> representation = new HashMap<>();
        representation.put("UUID", networkElement.getUUID().toString());
        representation.put("elementType", networkElement.getElementType().name().toLowerCase());
        representation.put("deviceType", networkElement.getDeviceType());
        representation.put("position", LuaConverter.posToObject(center.subtract(networkElement.getPos())));
        return representation;
    }
}