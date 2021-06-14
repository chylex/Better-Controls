package chylex.bettercontrols.gui;
import chylex.bettercontrols.gui.OptionListWidget.Entry;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class OptionListWidget extends ElementListWidget<Entry> {
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
	
	private static Offset getElementOffset(final Element element) {
		if (element instanceof Widget) {
			return new Offset(((Widget)element).getX(), ((Widget)element).getY());
		}
		else if (element instanceof ClickableWidget) {
			return new Offset(((ClickableWidget)element).x, ((ClickableWidget)element).y);
		}
		else {
			return new Offset(0, 0);
		}
	}
	
	public interface Widget extends Element, Drawable {
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
	
	public OptionListWidget(final int top, final int bottom, final int width, final int height, final List<Element> widgets, final int innerHeight) {
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
	protected int getScrollbarPositionX() {
		return (width + ROW_WIDTH) / 2 + 4;
	}
	
	protected static final class Entry extends ElementListWidget.Entry<Entry> {
		private final List<Element> elements;
		private final List<Selectable> selectables;
		private final Map<Element, Offset> offsets;
		
		public Entry(final List<Element> elements) {
			this.elements = new ArrayList<>(elements);
			this.selectables = elements.stream().filter(e -> e instanceof Selectable).map(e -> (Selectable)e).toList();
			this.offsets = elements.stream().collect(Collectors.toMap(Function.identity(), OptionListWidget::getElementOffset));
		}
		
		@Override
		public List<? extends Element> children() {
			return Collections.unmodifiableList(elements);
		}
		
		@Override
		public List<? extends Selectable> method_37025() {
			return Collections.unmodifiableList(selectables);
		}
		
		@Override
		public void render(final MatrixStack matrices, final int index, final int y, final int x, final int entryWidth, final int entryHeight, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta) {
			for (final Element element : elements) {
				final Offset offset = offsets.get(element);
				
				if (element instanceof final ClickableWidget button) {
					button.x = x + offset.x;
					button.y = y + offset.y;
				}
				else if (element instanceof final Widget widget) {
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				
				if (element instanceof final Drawable drawable) {
					drawable.render(matrices, mouseX, mouseY, tickDelta);
				}
			}
		}
	}
}
