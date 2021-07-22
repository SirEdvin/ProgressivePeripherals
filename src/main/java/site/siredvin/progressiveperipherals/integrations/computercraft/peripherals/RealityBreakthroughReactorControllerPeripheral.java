package site.siredvin.progressiveperipherals.integrations.computercraft.peripherals;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import de.srendi.advancedperipherals.common.addons.computercraft.base.BasePeripheral;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.tileentities.RealityBreakthroughRectorControllerTileEntity;

public class RealityBreakthroughReactorControllerPeripheral extends BasePeripheral implements IDynamicPeripheral {
    private final RealityBreakthroughRectorControllerTileEntity tileEntity;
    public RealityBreakthroughReactorControllerPeripheral(String type, RealityBreakthroughRectorControllerTileEntity tileEntity) {
        super(type, tileEntity);
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @NotNull
    @Override
    public String[] getMethodNames() {
        return tileEntity.getMethodNames();
    }

    @NotNull
    @Override
    public MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int methodIndex, @NotNull IArguments arguments) throws LuaException {
        return tileEntity.callMethod(access, context, methodIndex, arguments);
    }
}
