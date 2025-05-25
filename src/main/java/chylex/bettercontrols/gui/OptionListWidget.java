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
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toMap;

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
			return new Offset(widget.getX(), widget.getY(), widget.getHeight());
		}
		else if (element instanceof AbstractWidget widget) {
			return new Offset(widget.getX(), widget.getY(), widget.getHeight());
		}
		else {
			return new Offset(0, 0, 0);
		}
	}
	
	public interface OptionWidget extends GuiEventListener, Renderable {
		void setX(int x);
		int getX();
		
		void setY(int y);
		int getY();
		
		int getHeight();
	}
	
	private record Offset(int x, int y, int height) {}
	
	@SuppressWarnings("ThisEscapedInObjectConstruction")
	public OptionListWidget(int width, int height, int top, int innerHeight, List<GuiEventListener> widgets) {
		super(Minecraft.getInstance(), width, height, top, innerHeight);
		addEntry(new Entry(this, widgets));
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
	
	@Override
	protected void ensureVisible(@NotNull Entry entry) {
		// Scrolling to focused item is implemented in Entry.
	}
	
	protected static final class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		private final OptionListWidget parentWidget;
		private final List<GuiEventListener> elements;
		private final List<NarratableEntry> narratables;
		private final Map<GuiEventListener, Offset> offsets;
		
		public Entry(OptionListWidget parentWidget, List<GuiEventListener> elements) {
			this.parentWidget = parentWidget;
			this.elements = List.copyOf(elements);
			this.narratables = elements.stream().filter(e -> e instanceof NarratableEntry).map(e -> (NarratableEntry)e).toList();
			this.offsets = elements.stream().collect(toMap(Function.identity(), OptionListWidget::getElementOffset));
		}
		
		@Override
		public void setFocused(@Nullable GuiEventListener element) {
			super.setFocused(element);
			
			if (Minecraft.getInstance().getLastInputType().isKeyboard()) {
				Offset offset = offsets.get(element);
				if (offset != null) {
					parentWidget.setScrollAmount(offset.y + (offset.height * 0.5F) - (parentWidget.getHeight() * 0.5F) + 4);
				}
			}
		}
		
		@NotNull
		@Override
		public List<? extends GuiEventListener> children() {
			return elements;
		}
		
		@NotNull
		@Override
		public List<? extends NarratableEntry> narratables() {
			return narratables;
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
