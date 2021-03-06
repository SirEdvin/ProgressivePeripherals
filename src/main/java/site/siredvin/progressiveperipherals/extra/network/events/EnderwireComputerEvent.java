package site.siredvin.progressiveperipherals.extra.network.events;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EnderwireComputerEvent implements IEnderwireBusEvent {
    private final @NotNull Map<String, Object> data;
    private final int reachableRange;
    private final boolean interdimensional;
    private final String originalDimension;
    private final BlockPos pos;


    protected EnderwireComputerEvent(int reachableRange, boolean interdimensional, @NotNull String originalDimension, @NotNull BlockPos pos, @NotNull Map<String, Object> data) {
        this.reachableRange = reachableRange;
        this.interdimensional = interdimensional;
        this.originalDimension = originalDimension;
        this.pos = pos;
        this.data = data;
    }

    public @NotNull Map<String, Object> getData() {
        return data;
    }

    public boolean IsNotMalformed(BlockPos receiverPos, String receiverDimension) {
        return EnderwireNetwork.canReach(reachableRange, interdimensional, pos, receiverPos, originalDimension, receiverDimension);
    }

    public static EnderwireComputerEvent timed(@NotNull String name, int reachableRange, boolean interdimensional, @NotNull String originalDimension, @NotNull BlockPos pos, Map<String, Object> data) {
        LocalDateTime now = LocalDateTime.now();
        long epoch = now.toEpochSecond(ZoneOffset.UTC);

        data.put("datetime", now.format(DateTimeFormatter.ISO_DATE_TIME));
        data.put("epoch", epoch);
        data.put("event", name);

        return new EnderwireComputerEvent(reachableRange, interdimensional, originalDimension, pos, data);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
