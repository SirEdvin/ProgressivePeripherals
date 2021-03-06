package site.siredvin.progressiveperipherals.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseNBTBlock;
import site.siredvin.progressiveperipherals.common.setup.Blocks;
import site.siredvin.progressiveperipherals.common.tileentities.FlexibleRealityAnchorTileEntity;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import java.util.ArrayList;
import java.util.List;

public class FlexibleRealityAnchor extends BaseNBTBlock<FlexibleRealityAnchorTileEntity> {
    public static final BooleanProperty CONFIGURED = BooleanProperty.create("configured");
    public static final BooleanProperty PLAYER_PASSABLE = BooleanProperty.create("player_passable");
    public static final BooleanProperty LIGHT_PASSABLE = BooleanProperty.create("light_passable");
    public static final BooleanProperty SKY_LIGHT_PASSABLE = BooleanProperty.create("sky_light_passable");
    public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");

    private static final List<Property<?>> SAVABLE_PROPERTIES = new ArrayList<Property<?>>() {{
        add(PLAYER_PASSABLE);
        add(LIGHT_PASSABLE);
        add(SKY_LIGHT_PASSABLE);
        add(INVISIBLE);
    }};

    public FlexibleRealityAnchor() {
        super(BlockUtils.decoration().dynamicShape());
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(CONFIGURED, false)
                        .setValue(PLAYER_PASSABLE, false)
                        .setValue(LIGHT_PASSABLE, false)
                        .setValue(SKY_LIGHT_PASSABLE, false)
                        .setValue(INVISIBLE, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONFIGURED);
        builder.add(PLAYER_PASSABLE);
        builder.add(LIGHT_PASSABLE);
        builder.add(SKY_LIGHT_PASSABLE);
        builder.add(INVISIBLE);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof FlexibleRealityAnchorTileEntity)
            return ((FlexibleRealityAnchorTileEntity) tileEntity).getLightLevel();
        return 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public @NotNull TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FlexibleRealityAnchorTileEntity();
    }

    @NotNull
    @Override
    public ItemStack createItemStack() {
        return new ItemStack(Blocks.FLEXIBLE_REALITY_ANCHOR.get().asItem());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos, @NotNull ISelectionContext context) {
        if (state.getValue(INVISIBLE))
            return super.getShape(state, world, pos, context);
        FlexibleRealityAnchorTileEntity tileEntity = (FlexibleRealityAnchorTileEntity) world.getBlockEntity(pos);
        if (tileEntity != null) {
            BlockState mimicState = tileEntity.getMimic();
            if (mimicState != null)
                return tileEntity.getMimic().getShape(world, pos);
        }
        return super.getShape(state, world, pos, context);
    }

    @Override
    public @NotNull BlockRenderType getRenderShape(BlockState state) {
        if (state.getValue(INVISIBLE))
            return BlockRenderType.INVISIBLE;
        return super.getRenderShape(state);
    }

    @Override
    public List<Property<?>> savableProperties() {
        return SAVABLE_PROPERTIES;
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    public @NotNull VoxelShape getVisualShape(BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos, @NotNull ISelectionContext context) {
        if (state.getValue(LIGHT_PASSABLE) || !state.getValue(CONFIGURED))
            return VoxelShapes.empty();
        return super.getVisualShape(state, world, pos, context);
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos) {
        if (state.getValue(LIGHT_PASSABLE) || !state.getValue(CONFIGURED))
            return 1.0F;
        return super.getShadeBrightness(state, world, pos);
    }

    public boolean propagatesSkylightDown(BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos) {
        return state.getValue(SKY_LIGHT_PASSABLE) || !state.getValue(CONFIGURED);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull IBlockReader world, @NotNull BlockPos pos, ISelectionContext context) {
        Entity collidingEntity = context.getEntity();
        if (collidingEntity != null) {
            if (state.getValue(PLAYER_PASSABLE) && collidingEntity instanceof PlayerEntity)
                return VoxelShapes.empty();
        }
        return state.getShape(world, pos);
    }
}
