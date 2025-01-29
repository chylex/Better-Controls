package chylex.bettercontrols.gui.elements;

import chylex.bettercontrols.gui.OptionListWidget.OptionWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class TextWidget implements OptionWidget {
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	
	public static final int WHITE = 0xFF_FF_FF;
	
	private final Component text;
	private int x;
	private int y;
	private final int width;
	private final int height;
	private final int align;
	
	public TextWidget(int x, int y, int width, int height, Component text, int align) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.align = align;
	}
	
	public TextWidget(int x, int y, int width, Component text, int align) {
		this(x, y, width, 20, text, align);
	}
	
	public TextWidget(int x, int y, int width, Component text) {
		this(x, y, width, 20, text, LEFT);
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void setFocused(boolean focused) {}
	
	@Override
	public boolean isFocused() {
		return false;
	}
	
	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		Font textRenderer = Minecraft.getInstance().font;
		List<FormattedCharSequence> lines = textRenderer.split(text, width);
		final int lineHeight = textRenderer.lineHeight + 1;
		
		int finalX = align == CENTER ? x + (width / 2) - (lines.stream().mapToInt(textRenderer::width).max().orElse(0) / 2) : x;
		int finalY = y + (height / 2) - (lineHeight * lines.size() / 2) + 1;
		
		for (int i = 0; i < lines.size(); i++) {
			graphics.drawString(textRenderer, lines.get(i), finalX, finalY + (i * lineHeight), WHITE);
		}
	}
}
