package chylex.bettercontrols.gui;
import chylex.bettercontrols.gui.OptionListWidget.Entry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class OptionListWidget extends ContainerObjectSelectionList<Entry> {
	public static final int ROW_WIDTH = 408;
	public static final int ROW_PADDING = 2;
	
	public static final int COL2_W = (ROW_WIDTH / 2) - ROW_PADDING;
	public static final int COL4_W = (ROW_WIDTH / 4) - ROW_PADDING;
	
	public static int col2(final int column) {
		return (column * ROW_WIDTH) / 2;
	}
	
	public static int col4(final int column) {
		return (column * ROW_WIDTH) / 4;
	}
	
	private static Offset getElementOffset(final GuiEventListener element) {
		if (element instanceof OptionWidget) {
			return new Offset(((OptionWidget)element).getX(), ((OptionWidget)element).getY());
		}
		else if (element instanceof AbstractWidget) {
			return new Offset(((AbstractWidget)element).x, ((AbstractWidget)element).y);
		}
		else {
			return new Offset(0, 0);
		}
	}
	
	public interface OptionWidget extends GuiEventListener, Widget {
		int getX();
		int getY();
		void setX(int x);
		void setY(int y);
	}
	
	private static final class Offset {
		public final int x;
		public final int y;
		
		private Offset(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public OptionListWidget(final int top, final int bottom, final int width, final int height, final List<GuiEventListener> widgets, final int innerHeight) {
		super(MINECRAFT, width, height, top, bottom, innerHeight);
		addEntry(new Entry(widgets));
	}
	
	@Override
	public int getRowLeft() {
		return super.getRowLeft() - ROW_PADDING;
	}
	
	@Override
	public int getRowWidth() {
		return ROW_WIDTH;
	}
	
	@Override
	protected int getScrollbarPosition() {
		return (width + ROW_WIDTH) / 2 + 4;
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
		
		@Override
		public List<? extends GuiEventListener> children() {
			return Collections.unmodifiableList(elements);
		}
		
		@Override
		public List<? extends NarratableEntry> narratables() {
			return Collections.unmodifiableList(narratables);
		}
		
		@Override
		public void render(final PoseStack matrices, final int index, final int y, final int x, final int entryWidth, final int entryHeight, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
			for (final GuiEventListener element : elements) {
				final Offset offset = offsets.get(element);
				
				if (element instanceof AbstractWidget) {
					final AbstractWidget button = (AbstractWidget)element;
					button.x = x + offset.x;
					button.y = y + offset.y;
				}
				else if (element instanceof OptionWidget) {
					final OptionWidget widget = (OptionWidget)element;
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				
				if (element instanceof Widget) {
					((Widget)element).render(matrices, mouseX, mouseY, tickDelta);
				}
			}
		}
	}
}
