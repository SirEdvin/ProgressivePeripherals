package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.IPeripheralTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleStatueTileEntity;
import site.siredvin.progressiveperipherals.utils.LuaUtils;
import site.siredvin.progressiveperipherals.utils.dao.QuadList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StatueWorkbenchPeripheral extends BasePeripheral {
    public <T extends TileEntity & IPeripheralTileEntity> StatueWorkbenchPeripheral(String type, T tileEntity) {
        super(type, tileEntity);
    }

    @Override
    public boolean isEnabled() {
        return ProgressivePeripheralsConfig.enableStatueWorkbench;
    }

    protected Optional<FlexibleStatueTileEntity> getStatue() {
        TileEntity tileEntity = getWorld().getBlockEntity(getPos().offset(0, 1, 0));
        if (!(tileEntity instanceof FlexibleStatueTileEntity))
            return Optional.empty();
        return Optional.of((FlexibleStatueTileEntity) tileEntity);
    }

    @LuaFunction(mainThread = true)
    public final boolean isPresent() {
        return getStatue().isPresent();
    }

    @LuaFunction(mainThread = true)
    public final MethodResult setStatueName(String name) {
        Optional<FlexibleStatueTileEntity> opStatue = getStatue();
        if (!opStatue.isPresent())
            return MethodResult.of(null, "Cannot find statue on top of workbench");
        opStatue.ifPresent(statue -> {
            statue.setName(name);
        });
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getAuthor() {
        Optional<FlexibleStatueTileEntity> opStatue = getStatue();
        if (!opStatue.isPresent())
            return MethodResult.of(null, "Cannot find statue on top of workbench");
        FlexibleStatueTileEntity tileEntity = opStatue.get();
        return MethodResult.of(tileEntity.getAuthor());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult setAuthor(String author) {
        Optional<FlexibleStatueTileEntity> opStatue = getStatue();
        if (!opStatue.isPresent())
            return MethodResult.of(null, "Cannot find statue on top of workbench");
        opStatue.ifPresent(statue -> {
            statue.setAuthor(author);
        });
        return MethodResult.of(true);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getStatueName() {
        Optional<FlexibleStatueTileEntity> opStatue = getStatue();
        if (!opStatue.isPresent())
            return MethodResult.of(null, "Cannot find statue on top of workbench");
        FlexibleStatueTileEntity tileEntity = opStatue.get();
        return MethodResult.of(tileEntity.getName());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getQuads() {
        Optional<FlexibleStatueTileEntity> opStatue = getStatue();
        if (!opStatue.isPresent())
            return MethodResult.of(null, "Cannot find statue on top of workbench");
        FlexibleStatueTileEntity tileEntity = opStatue.get();
        QuadList quadList = tileEntity.getBakedQuads();
        if (quadList == null)
            return MethodResult.of(new HashMap<>());
        return MethodResult.of(quadList.toLua());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult setQuads(Map<?, ?> quads) throws LuaException {
        Optional<FlexibleStatueTileEntity> opStatue = getStatue();
        if (!opStatue.isPresent())
            return MethodResult.of(null, "Cannot find statue on top of workbench");
        QuadList quadList = LuaUtils.convertToQuadList(quads);
        FlexibleStatueTileEntity tileEntity = opStatue.get();
        tileEntity.setBakedQuads(quadList);
        return MethodResult.of(true);
    }
}
