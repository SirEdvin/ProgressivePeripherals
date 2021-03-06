package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleRealityAnchor;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;

public class FlexibleRealityAnchorTileEntity extends MutableNBTTileEntity<BasePeripheral<?>> implements ITileEntityDataProvider {

    private static final String MIMIC_TAG = "mimic";
    private static final String LIGHT_LEVEL_TAG = "lightLevel";

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic;
    private int lightLevel = 0;
    private @Nullable BlockState pendingState;

    public FlexibleRealityAnchorTileEntity() {
        super(TileEntityTypes.FLEXIBLE_REALITY_ANCHOR.get());
    }

    @Override
    public void pushInternalDataChangeToClient() {
        if (pendingState != null) {
            pushInternalDataChangeToClient(pendingState);
            pendingState = null;
        } else {
            pushInternalDataChangeToClient(getBlockState());
        }
    }

    public void setMimic(@Nullable BlockState mimic, boolean skipUpdate) {
        setMimic(mimic, getBlockState(), skipUpdate);
    }

    public void setMimic(@Nullable BlockState mimic, @NotNull BlockState state, boolean skipUpdate) {
        if (mimic != null) {
            ResourceLocation blockName = mimic.getBlock().getRegistryName();
            if (blockName != null && ProgressivePeripheralsConfig.realityForgerBlacklist.contains(blockName.toString()))
                return;
        }
        this.mimic = mimic;
        if (!skipUpdate) {
            pushInternalDataChangeToClient(state.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null));
        } else {
            if (pendingState == null)
                pendingState = state;
            pendingState = pendingState.setValue(FlexibleRealityAnchor.CONFIGURED, mimic != null);
        }
    }

    public BlockState getMimic() {
        return this.mimic;
    }

    public void setBooleanStateValue(BooleanProperty stateValue, boolean value) {
        if (pendingState == null)
            pendingState = getBlockState();
        pendingState = pendingState.setValue(stateValue, value);
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = Math.max(0, Math.min(lightLevel, 15));
    }

    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public @NotNull IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, mimic)
                .build();
    }

    @Override
    public CompoundNBT saveInternalData(CompoundNBT data) {
        if (mimic != null) {
            data.put(MIMIC_TAG, NBTUtil.writeBlockState(mimic));
        }
        if (lightLevel != 0)
            data.putInt(LIGHT_LEVEL_TAG, lightLevel);
        return data;
    }

    @Override
    public boolean isRequiredRenderUpdate() {
        return true;
    }

    @Override
    public void loadInternalData(BlockState state, CompoundNBT data) {
        if (data.contains(MIMIC_TAG)) {
            setMimic(NBTUtil.readBlockState(data.getCompound(MIMIC_TAG)), state, true);
        }
        if (data.contains(LIGHT_LEVEL_TAG))
            setLightLevel(data.getInt(LIGHT_LEVEL_TAG));
    }
}
