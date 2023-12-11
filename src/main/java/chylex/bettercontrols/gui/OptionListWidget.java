package chylex.bettercontrols.gui;

import chylex.bettercontrols.gui.OptionListWidget.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OptionListWidget extends ContainerObjectSelectionList<Entry> {
	public static final int ROW_WIDTH = 408;
	
	private static final int ROW_HORIZONTAL_PADDING = 2;
	private static final int SCROLL_MULTIPLIER = 18;
	
	public static final int COL2_W = (ROW_WIDTH / 2) - ROW_HORIZONTAL_PADDING;
	public static final int COL4_W = (ROW_WIDTH / 4) - ROW_HORIZONTAL_PADDING;
	
	public static int col2(final int column) {
		return (column * ROW_WIDTH) / 2;
	}
	
	public static int col4(final int column) {
		return (column * ROW_WIDTH) / 4;
	}
	
	private static Offset getElementOffset(final GuiEventListener element) {
		if (element instanceof final OptionWidget widget) {
			return new Offset(widget.getX(), widget.getY());
		}
		else if (element instanceof final AbstractWidget widget) {
			return new Offset(widget.getX(), widget.getY());
		}
		else {
			return new Offset(0, 0);
		}
	}
	
	public interface OptionWidget extends GuiEventListener, Renderable {
		int getX();
		int getY();
		void setX(int x);
		void setY(int y);
	}
	
	private record Offset(int x, int y) {}
	
	public OptionListWidget(final int top, final int bottom, final int width, final int height, final List<GuiEventListener> widgets, final int innerHeight) {
		super(Minecraft.getInstance(), width, height, top, bottom, innerHeight);
		addEntry(new Entry(widgets));
	}
	
	@Override
	public int getRowLeft() {
		return super.getRowLeft() - ROW_HORIZONTAL_PADDING;
	}
	
	@Override
	public int getRowWidth() {
		return ROW_WIDTH;
	}
	
	@Override
	protected int getScrollbarPosition() {
		return (width + ROW_WIDTH) / 2 + 4;
	}
	
	@Override
	public boolean mouseScrolled(final double x, final double y, final double xAmount, final double yAmount) {
		setScrollAmount(getScrollAmount() - yAmount * SCROLL_MULTIPLIER);
		return true;
	}
	
	protected static final class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		private final List<GuiEventListener> elements;
		private final List<NarratableEntry> narratables;
		private final Map<GuiEventListener, Offset> offsets;
		
		public Entry(final List<GuiEventListener> elements) {
			this.elements = new ArrayList<>(elements);
			this.narratables = elements.stream().filter(e -> e instanceof NarratableEntry).map(e -> (NarratableEntry)e).collect(Collectors.toList());
			this.offsets = elements.stream().collect(Collectors.toMap(Function.identity(), OptionListWidget::getElementOffset));
		}
		
		@NotNull
		@Override
		public List<? extends GuiEventListener> children() {
			return Collections.unmodifiableList(elements);
		}
		
		@NotNull
		@Override
		public List<? extends NarratableEntry> narratables() {
			return Collections.unmodifiableList(narratables);
		}
		
		@Override
		public void render(final @NotNull GuiGraphics graphics, final int index, final int y, final int x, final int entryWidth, final int entryHeight, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
			for (final GuiEventListener element : elements) {
				final Offset offset = offsets.get(element);
				
				if (element instanceof final AbstractWidget widget) {
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				else if (element instanceof final OptionWidget widget) {
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				
				if (element instanceof final Renderable renderable) {
					renderable.render(graphics, mouseX, mouseY, tickDelta);
				}
			}
		}
	}
}
