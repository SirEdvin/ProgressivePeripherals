package site.siredvin.progressiveperipherals.common.tileentities;

import de.srendi.advancedperipherals.lib.peripherals.BasePeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.integrations.IProbeable;
import site.siredvin.progressiveperipherals.api.tileentity.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleStatue;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.common.tileentities.base.MutableNBTTileEntity;
import site.siredvin.progressiveperipherals.utils.NBTUtils;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlexibleStatueTileEntity extends MutableNBTTileEntity<BasePeripheral<?>> implements ITileEntityDataProvider, IProbeable {
    public static final String BAKED_QUADS_TAG = "bakedQuads";
    public static final String NAME_TAG = "statueName";
    public static final String AUTHOR_TAG = "statueAuthor";
    public static final String LIGHT_LEVEL_TAG = "lightLevel";

    public static final ModelProperty<QuadList> BAKED_QUADS = new ModelProperty<>();

    private @Nullable BlockState pendingState;
    private @Nullable QuadList bakedQuads;
    private @Nullable VoxelShape blockShape;
    private @Nullable String name;
    private @Nullable String author;
    private int lightLevel = 0;

    public FlexibleStatueTileEntity() {
        super(TileEntityTypes.FLEXIBLE_STATUE.get());
    }

    @Override
    public @NotNull IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(BAKED_QUADS, bakedQuads).build();
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

    @Override
    public boolean isRequiredRenderUpdate() {
        return true;
    }

    public void loadInternalData(BlockState state, CompoundNBT tag) {
        if (tag.contains(NAME_TAG))
            setName(tag.getString(NAME_TAG));
        if (tag.contains(AUTHOR_TAG))
            setAuthor(tag.getString(AUTHOR_TAG));
        if (tag.contains(LIGHT_LEVEL_TAG))
            setLightLevel(tag.getInt(LIGHT_LEVEL_TAG));
        if (tag.contains(BAKED_QUADS_TAG)) {
            QuadList newBakedQuads = NBTUtils.readQuadList(tag.getByteArray(BAKED_QUADS_TAG));
            if (!Objects.equals(bakedQuads, newBakedQuads)) {
                if (newBakedQuads != null) {
                    setBakedQuads(newBakedQuads, state,true);
                } else {
                    clear(state,true);
                }
            }
        }
    }

    public CompoundNBT saveInternalData(CompoundNBT tag) {
        if (bakedQuads != null) {
            INBT data = NBTUtils.serialize(bakedQuads);
            if (data != null)
                tag.put(BAKED_QUADS_TAG, data);
        }
        if (name != null) {
            tag.putString(NAME_TAG, name);
        }
        if (author != null) {
            tag.putString(AUTHOR_TAG, author);
        }
        if (lightLevel != 0)
            tag.putInt(LIGHT_LEVEL_TAG, lightLevel);
        return tag;
    }

    public void setBakedQuads(@NotNull QuadList bakedQuads, boolean skipUpdate) {
        setBakedQuads(bakedQuads, getBlockState(), skipUpdate);
    }

    public void setBakedQuads(@NotNull QuadList bakedQuads, BlockState state, boolean skipUpdate) {
        this.bakedQuads = bakedQuads;
        refreshShape();
        if (!skipUpdate) {
            pushInternalDataChangeToClient(state.setValue(FlexibleStatue.CONFIGURED, true));
        } else {
            if (pendingState == null)
                pendingState = state;
            pendingState = pendingState.setValue(FlexibleStatue.CONFIGURED, true);
        }
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setAuthor(@NotNull String author) {
        this.author = author;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = Math.max(0, Math.min(lightLevel, 15));
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable String getAuthor() {
        return author;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void clear(BlockState state, boolean skipUpdate) {
        bakedQuads = null;
        refreshShape();
        if (!skipUpdate) {
            pushInternalDataChangeToClient(state.setValue(FlexibleStatue.CONFIGURED, false));
        } else {
            if (pendingState == null)
                pendingState = state;
            pendingState = pendingState.setValue(FlexibleStatue.CONFIGURED, false);
        }
    }

    public void refreshShape() {
        if (bakedQuads == null) {
            blockShape = null;
        } else {
            blockShape = bakedQuads.shape();
        }
    }

    public @Nullable VoxelShape getBlockShape() {
        return blockShape;
    }

    public @Nullable QuadList getBakedQuads() {
        return bakedQuads;
    }


    @Override
    public List<ITextComponent> commonProbeData(BlockState state) {
        List<ITextComponent> data = new ArrayList<>();
        if (name != null)
            data.add(TranslationUtil.localization("statue_name").append(name));
        if (author != null)
            data.add(TranslationUtil.localization("statue_author").append(author));
        return data;
    }
}
