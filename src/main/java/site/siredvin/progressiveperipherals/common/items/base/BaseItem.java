package site.siredvin.progressiveperipherals.common.items.base;

import de.srendi.advancedperipherals.common.util.EnumColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.List;
import java.util.function.Supplier;

public class BaseItem extends Item {
    protected ITextComponent description;
    private final Supplier<Boolean> enabledSup;

    public BaseItem(Properties properties) {
        this(properties, () -> true);
    }

    public BaseItem(Properties properties, Supplier<Boolean> enabledSup) {
        super(properties.tab(ProgressivePeripherals.TAB));
        this.enabledSup = enabledSup;
    }

    public boolean isEnabled() {
        return enabledSup.get();
    }

    @SuppressWarnings("unused")
    public void appendModHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ITextComponent modDescription = getModDescription();
        if (!modDescription.getString().isEmpty())
            tooltip.add(EnumColor.buildTextComponent(modDescription));
        if (!isEnabled())
            tooltip.add(EnumColor.buildTextComponent(new TranslationTextComponent("item.advancedperipherals.tooltip.disabled")));
    }

    public @NotNull ITextComponent getModDescription() {
        if (description == null)
            description = TranslationUtil.itemTooltip(getDescriptionId());
        return description;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        appendModHoverText(stack, worldIn, tooltip, flagIn);
    }
}
