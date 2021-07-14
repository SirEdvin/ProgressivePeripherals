package site.siredvin.ancientperipherals.computercraft.turtles;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.base.ModelTransformingTurtle;
import jdk.incubator.jpackage.internal.resources.ResourceLocator;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import site.siredvin.ancientperipherals.AncientPeripherals;
import site.siredvin.ancientperipherals.common.setup.Items;
import site.siredvin.ancientperipherals.computercraft.peripherals.EnchantingAutomataCorePeripheral;
import site.siredvin.ancientperipherals.utils.TranslationUtil;

public class EnchantingTurtle extends ModelTransformingTurtle<EnchantingAutomataCorePeripheral> {
    public static final ResourceLocation ID = new ResourceLocation(AncientPeripherals.MOD_ID, "enchanting_automata");
    public EnchantingTurtle() {
        super(ID, TranslationUtil.turtle("enchanting"), new ItemStack(Items.ENCHANTING_AUTOMATA_CORE.get()));
    }

    @Override
    protected ModelResourceLocation getLeftModel() {
        return null;
    }

    @Override
    protected ModelResourceLocation getRightModel() {
        return null;
    }

    @Override
    protected EnchantingAutomataCorePeripheral buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new EnchantingAutomataCorePeripheral("enchantingAutomataCore", turtle, side);
    }
}
