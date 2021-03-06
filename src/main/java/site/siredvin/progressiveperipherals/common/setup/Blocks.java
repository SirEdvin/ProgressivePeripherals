package site.siredvin.progressiveperipherals.common.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import site.siredvin.progressiveperipherals.common.blocks.*;
import site.siredvin.progressiveperipherals.common.blocks.base.BaseBlock;
import site.siredvin.progressiveperipherals.common.blocks.enderwire.*;
import site.siredvin.progressiveperipherals.common.blocks.machinery.*;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import site.siredvin.progressiveperipherals.common.items.FlexibleRealityAnchorItem;
import site.siredvin.progressiveperipherals.common.items.FlexibleStatueItem;
import site.siredvin.progressiveperipherals.common.items.base.BaseBlockItem;
import site.siredvin.progressiveperipherals.common.items.peripheral.PeripheralBlockItem;
import site.siredvin.progressiveperipherals.common.tileentities.enderwire.*;
import site.siredvin.progressiveperipherals.extra.network.api.EnderwireElementType;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery.CreativeDecryptPlugin;
import site.siredvin.progressiveperipherals.integrations.computercraft.plugins.machinery.CreativePowerGeneratorPlugin;
import site.siredvin.progressiveperipherals.utils.BlockUtils;

import java.util.function.Supplier;

public class Blocks {

    @SuppressWarnings("EmptyMethod")
    static void register() {
    }

    // Peripherals
    public static final RegistryObject<Block> REALITY_FORGER = register(
            "reality_forger",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.REALITY_FORGER),
            () -> new PeripheralBlockItem(Blocks.REALITY_FORGER.get(), () -> ProgressivePeripheralsConfig.enableRealityForger)
    );

    public static final RegistryObject<Block> REALITY_FORGER_MK2 = register(
            "reality_forger_mk2",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.REALITY_FORGER),
            () -> new PeripheralBlockItem(Blocks.REALITY_FORGER_MK2.get(), () -> ProgressivePeripheralsConfig.enableRealityForger)
    );

    public static final RegistryObject<Block> STATUE_WORKBENCH = register(
            "statue_workbench",
            StatueWorkbench::new,
            () -> new PeripheralBlockItem(Blocks.STATUE_WORKBENCH.get(), () -> ProgressivePeripheralsConfig.enableStatueWorkbench)
    );

    public static final RegistryObject<Block> ABSTRACTIUM_PEDESTAL = register(
            "abstractium_pedestal",
            AbstractiumPedestal::new,
            () -> new PeripheralBlockItem(Blocks.ABSTRACTIUM_PEDESTAL.get(), () -> ProgressivePeripheralsConfig.enableAbstractiumPedestal)
    );

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_REACTOR_CONTROLLER = register(
            "reality_breakthrough_reactor_controller",
            () -> new MachineryController<>(TileEntityTypes.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER),
            () -> new PeripheralBlockItem(Blocks.REALITY_BREAKTHROUGH_REACTOR_CONTROLLER.get(), () -> ProgressivePeripheralsConfig.enableReactor)
    );

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER = register(
            "reality_breakthrough_extractor_controller",
            () -> new MachineryController<>(TileEntityTypes.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER),
            () -> new PeripheralBlockItem(Blocks.REALITY_BREAKTHROUGH_EXTRACTOR_CONTROLLER.get(), () -> ProgressivePeripheralsConfig.enableExtractor)
    );

    public static final RegistryObject<Block> RECIPE_REGISTRY = register(
            "recipe_registry",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.RECIPE_REGISTRY),
            () -> new PeripheralBlockItem(Blocks.RECIPE_REGISTRY.get(), () -> ProgressivePeripheralsConfig.enableRecipeRegistry)
    );

    public static final RegistryObject<Block> ITEM_REGISTRY = register(
            "item_registry",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.ITEM_REGISTRY),
            () -> new PeripheralBlockItem(Blocks.ITEM_REGISTRY.get(), () -> ProgressivePeripheralsConfig.enableItemRegistry)
    );

    public static final RegistryObject<Block> EVENT_DISTRIBUTOR = register(
            "event_distributor",
            () -> new GenericTileEntityBlock<>(TileEntityTypes.EVENT_DISTRIBUTOR),
            () -> new PeripheralBlockItem(Blocks.EVENT_DISTRIBUTOR.get(), () -> ProgressivePeripheralsConfig.enableEventDistributor)
    );

    // Utility

    public static final RegistryObject<Block> FLEXIBLE_REALITY_ANCHOR = register(
            "flexible_reality_anchor", FlexibleRealityAnchor::new, FlexibleRealityAnchorItem::new
    );

    public static final RegistryObject<Block> FLEXIBLE_STATUE = register(
            "flexible_statue", FlexibleStatue::new, FlexibleStatueItem::new
    );

    public static final RegistryObject<Block> IRREALIUM_PEDESTAL = register(
            "irrealium_pedestal",
            IrrealiumPedestal::new,
            () -> new BaseBlockItem(Blocks.IRREALIUM_PEDESTAL.get())
    );

    public static final RegistryObject<Block> CREATIVE_ITEM_DUPLICATOR = register(
            "creative_item_duplicator",
            CreativeItemDuplicatorBlock::new,
            () -> new BaseBlockItem(Blocks.CREATIVE_ITEM_DUPLICATOR.get())
    );

    // machinery

    public static final RegistryObject<Block> IRREALIUM_MACHINERY_CASING = register(
            "irrealium_machinery_casing",
            () -> new MachineryBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_MACHINERY_CASING.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_MACHINERY_GLASS = register(
            "irrealium_machinery_glass",
            () -> new MachineryGlass(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_MACHINERY_GLASS.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_MACHINERY_IO_PORT = register(
            "irrealium_machinery_io_port",
            () -> new MachineryBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_MACHINERY_IO_PORT.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_MACHINERY_STORAGE = register(
            "irrealium_machinery_storage",
            () -> new MachineryStorage(BlockUtils.defaultProperties(), 27),
            () -> new BaseBlockItem(Blocks.IRREALIUM_MACHINERY_STORAGE.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_DOUBLE_MACHINERY_STORAGE = register(
            "irrealium_double_machinery_storage",
            () -> new MachineryStorage(BlockUtils.defaultProperties(), 54),
            () -> new BaseBlockItem(Blocks.IRREALIUM_DOUBLE_MACHINERY_STORAGE.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_MACHINERY_CREATIVE_DECRYPTOR = register(
            "irrealium_machinery_creative_decryptor",
            () -> new MachineryPluggableBlock(BlockUtils.defaultProperties(), new CreativeDecryptPlugin<>()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_MACHINERY_CREATIVE_DECRYPTOR.get(), new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Block> IRREALIUM_MACHINERY_CREATIVE_POWER_GENERATOR = register(
            "irrealium_machinery_creative_power_generator",
            () -> new MachineryPluggableBlock(BlockUtils.defaultProperties(), new CreativePowerGeneratorPlugin<>()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_MACHINERY_CREATIVE_POWER_GENERATOR.get(), new Item.Properties().stacksTo(1))
    );

    // Enderwire network

    public static final RegistryObject<Block> ENREDIUM_BLOCK = register(
            "enredium_block",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.ENREDIUM_BLOCK.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_CASING = register(
            "enderwire/casing",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_CASING.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_PERIPHERAL_CASING = register(
            "enderwire/peripheral_casing",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_PERIPHERAL_CASING.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_NETWORK_CONNECTOR = register(
            "enderwire/network_connector",
            () -> new EnderwireHorizontalBlock(EnderwireNetworkConnectorTileEntity::new),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_NETWORK_CONNECTOR.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_NETWORK_AMPLIFIER = register(
            "enderwire/network_amplifier",
            () -> new EnderwireHorizontalBlock(EnderwireNetworkAmplifierTileEntity::new),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_NETWORK_AMPLIFIER.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_DIMENSION_BREAKER = register(
            "enderwire/dimension_breaker",
            () -> new EnderwireHorizontalBlock(EnderwireDimensionBreakerTileEntity::new),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_DIMENSION_BREAKER.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_LEVER = register(
            "enderwire/lever",
            () -> new EnderwireLever(false),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_LEVER.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_BUTTON = register(
            "enderwire/button",
            () -> new EnderwireButton(false),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_BUTTON.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_PRESSURE_PLATE = register(
            "enderwire/pressure_plate",
            () -> new EnderwirePressurePlate(false),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_PRESSURE_PLATE.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ADVANCED_ENDERWIRE_LEVER = register(
            "enderwire/advanced_lever",
            () -> new EnderwireLever(true),
            () -> new BaseBlockItem(Blocks.ADVANCED_ENDERWIRE_LEVER.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ADVANCED_ENDERWIRE_BUTTON = register(
            "enderwire/advanced_button",
            () -> new EnderwireButton(true),
            () -> new BaseBlockItem(Blocks.ADVANCED_ENDERWIRE_BUTTON.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ADVANCED_ENDERWIRE_PRESSURE_PLATE = register(
            "enderwire/advanced_pressure_plate",
            () -> new EnderwirePressurePlate(true),
            () -> new BaseBlockItem(Blocks.ADVANCED_ENDERWIRE_PRESSURE_PLATE.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_REDSTONE_SENSOR = register(
            "enderwire/redstone_sensor",
            EnderwireRedstoneSensorBlock::new,
            () -> new BaseBlockItem(Blocks.ENDERWIRE_REDSTONE_SENSOR.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_REDSTONE_EMITTER = register(
            "enderwire/redstone_emitter",
            EnderwireRedstoneEmitterBlock::new,
            () -> new BaseBlockItem(Blocks.ENDERWIRE_REDSTONE_EMITTER.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_LIGHT_EMITTER = register(
            "enderwire/light_emitter",
            EnderwireLightEmitterBlock::new,
            () -> new BaseBlockItem(Blocks.ENDERWIRE_LIGHT_EMITTER.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_PERIPHERAL_SHARING = register(
            "enderwire/peripheral_sharing",
            () -> new EnderwireDirectionalBlock(EnderwirePeripheralSharingTileEntity::new),
            () -> new BaseBlockItem(Blocks.ENDERWIRE_PERIPHERAL_SHARING.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> ENDERWIRE_MODEM = register(
            "enderwire/modem",
            () -> new EnderwireHorizontalBlock(EnderwireModemTileEntity::new),
            () -> new PeripheralBlockItem(
                    Blocks.ENDERWIRE_MODEM.get(), new Item.Properties().stacksTo(64),
                    EnderwireElementType.MODEM::isEnabled
            )
    );


    // World feature

    public static final RegistryObject<Block> REALITY_BREAKTHROUGH_POINT = register(
            "reality_breakthrough_point",
            RealityBreakthroughPoint::new,
            () -> new BaseBlockItem(Blocks.REALITY_BREAKTHROUGH_POINT.get())
    );

    // Other

    public static final RegistryObject<Block> ABSTRACTIUM_BLOCK = register(
            "abstractium_block",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.ABSTRACTIUM_BLOCK.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> IRREALIUM_BLOCK = register(
            "irrealium_block",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.IRREALIUM_BLOCK.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> KNOWLEDGIUM_BLOCK = register(
            "knowledgium_block",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.KNOWLEDGIUM_BLOCK.get(), new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Block> KNOWLEDGIUM_CASING = register(
            "knowledgium_casing",
            () -> new BaseBlock(BlockUtils.defaultProperties()),
            () -> new BaseBlockItem(Blocks.KNOWLEDGIUM_CASING.get(), new Item.Properties().stacksTo(64))
    );

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return Registration.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Supplier<BlockItem> blockItem) {
        RegistryObject<T> registryObject = registerNoItem(name, block);
        Registration.ITEMS.register(name, blockItem);
        return registryObject;
    }

}