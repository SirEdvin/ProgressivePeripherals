package site.siredvin.progressiveperipherals.common.items;

import de.srendi.advancedperipherals.common.util.EnumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.List;

@SuppressWarnings("unused")
public class ControllingHelmet extends ArmorItem {
    private ITextComponent description;

    public ControllingHelmet() {
        super(ArmorMaterial.LEATHER, EquipmentSlotType.HEAD, new Properties().tab(ProgressivePeripherals.TAB).stacksTo(1));
    }

    @SuppressWarnings("SameReturnValue")
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull ITextComponent getDescription() {
        if (description == null)
            description = TranslationUtil.itemTooltip(getDescriptionId());
        return description;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.hold_ctrl")));
        } else {
            tooltip.add(EnumColor.buildTextComponent(getDescription()));
        }
        if (!isEnabled())
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.disabled")));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return ProgressivePeripherals.MOD_ID + ":textures/models/controlling_helmet.png";
    }

}
