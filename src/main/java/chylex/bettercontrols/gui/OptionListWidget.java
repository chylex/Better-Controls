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
	
	public static int col2(int column) {
		return (column * ROW_WIDTH) / 2;
	}
	
	public static int col4(int column) {
		return (column * ROW_WIDTH) / 4;
	}
	
	private static Offset getElementOffset(GuiEventListener element) {
		if (element instanceof OptionWidget widget) {
			return new Offset(widget.getX(), widget.getY());
		}
		else if (element instanceof AbstractWidget widget) {
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
	
	public OptionListWidget(int width, int height, int top, int innerHeight, List<GuiEventListener> widgets) {
		super(Minecraft.getInstance(), width, height, top, innerHeight);
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
	protected int scrollBarX() {
		return (width + ROW_WIDTH) / 2 + 4;
	}
	
	@Override
	public boolean mouseScrolled(double x, double y, double xAmount, double yAmount) {
		setScrollAmount(scrollAmount() - yAmount * SCROLL_MULTIPLIER);
		return true;
	}
	
	protected static final class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		private final List<GuiEventListener> elements;
		private final List<NarratableEntry> narratables;
		private final Map<GuiEventListener, Offset> offsets;
		
		public Entry(List<GuiEventListener> elements) {
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
		public void render(@NotNull GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			for (GuiEventListener element : elements) {
				Offset offset = offsets.get(element);
				
				if (element instanceof AbstractWidget widget) {
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				else if (element instanceof OptionWidget widget) {
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				
				if (element instanceof Renderable renderable) {
					renderable.render(graphics, mouseX, mouseY, tickDelta);
				}
			}
		}
	}
}
