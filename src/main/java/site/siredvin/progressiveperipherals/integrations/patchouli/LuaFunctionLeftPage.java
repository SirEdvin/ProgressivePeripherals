package site.siredvin.progressiveperipherals.integrations.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.List;

public class LuaFunctionLeftPage extends BookPage {

    @SerializedName("throw") IVariable can_throw;
    IVariable functionName;
    IVariable description;
    IVariable operationGroup;

    transient ITextComponent actualTitle;
    transient BookTextRenderer textRenderer;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        actualTitle = functionName.as(ITextComponent.class);

        TextBuilder builder = TextBuilder.start((IFormattableTextComponent) description.as(ITextComponent.class)).p();
        if (operationGroup != null)
            builder.local("operation_group").add((IFormattableTextComponent) operationGroup.as(ITextComponent.class)).p();
        if (can_throw != null) {
            builder.local("throw");
            List<IVariable> throwReasons = can_throw.asList();
            for (IVariable throwReason: throwReasons) {
                builder.addBulletListElement((IFormattableTextComponent) throwReason.as(ITextComponent.class));
            }
        }
        textRenderer = new BookTextRenderer(parent, builder.build(), 0, LuaFunctionPage.STARTING_HEIGHT);

    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        parent.drawCenteredStringNoShadow(ms, i18n(actualTitle.getString()), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        GuiBook.drawSeparator(ms, book, 0, 12);
        textRenderer.render(ms, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, int mouseButton) {
        return textRenderer.click(mouseX, mouseY, mouseButton);
    }
}
