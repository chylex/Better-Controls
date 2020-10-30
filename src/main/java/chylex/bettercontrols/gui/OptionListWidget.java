package chylex.bettercontrols.gui;
import chylex.bettercontrols.gui.OptionListWidget.Entry;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static chylex.bettercontrols.util.Statics.MINECRAFT;

public final class OptionListWidget extends AbstractOptionList<Entry>{
	public static final int ROW_WIDTH = 408;
	public static final int ROW_PADDING = 2;
	
	public static final int COL2_W = (ROW_WIDTH / 2) - ROW_PADDING;
	public static final int COL4_W = (ROW_WIDTH / 4) - ROW_PADDING;
	
	public static int col2(final int column){
		return (column * ROW_WIDTH) / 2;
	}
	
	public static int col4(final int column){
		return (column * ROW_WIDTH) / 4;
	}
	
	private static Offset getElementOffset(final IGuiEventListener element){
		if (element instanceof Widget){
			return new Offset(((Widget)element).getX(), ((Widget)element).getY());
		}
		else if (element instanceof net.minecraft.client.gui.widget.Widget){
			return new Offset(((net.minecraft.client.gui.widget.Widget)element).x, ((net.minecraft.client.gui.widget.Widget)element).y);
		}
		else{
			return new Offset(0, 0);
		}
	}
	
	public interface Widget extends IGuiEventListener, IRenderable{
		int getX();
		int getY();
		void setX(int x);
		void setY(int y);
	}
	
	private static final class Offset{
		public final int x;
		public final int y;
		
		private Offset(final int x, final int y){
			this.x = x;
			this.y = y;
		}
	}
	
	public OptionListWidget(final int top, final int bottom, final int width, final int height, final List<IGuiEventListener> widgets, final int innerHeight){
		super(MINECRAFT, width, height, top, bottom, innerHeight);
		addEntry(new Entry(widgets));
	}
	
	@Override
	protected int getRowLeft(){
		return super.getRowLeft() - ROW_PADDING;
	}
	
	@Override
	public int getRowWidth(){
		return ROW_WIDTH;
	}
	
	@Override
	protected int getScrollbarPosition(){
		return (width + ROW_WIDTH) / 2 + 4;
	}
	
	protected static final class Entry extends AbstractOptionList.Entry<Entry>{
		private final List<IGuiEventListener> elements;
		private final Map<IGuiEventListener, Offset> offsets;
		
		public Entry(final List<IGuiEventListener> elements){
			this.elements = new ArrayList<>(elements);
			this.offsets = elements.stream().collect(Collectors.toMap(Function.identity(), OptionListWidget::getElementOffset));
		}
		
		@Override
		public List<? extends IGuiEventListener> children(){
			return Collections.unmodifiableList(elements);
		}
		
		@Override
		public void render(final int index, final int y, final int x, final int entryWidth, final int entryHeight, final int mouseX, final int mouseY, final boolean hovered, final float tickDelta){
			for(final IGuiEventListener element : elements){
				final Offset offset = offsets.get(element);
				
				if (element instanceof net.minecraft.client.gui.widget.Widget){
					final net.minecraft.client.gui.widget.Widget button = (net.minecraft.client.gui.widget.Widget)element;
					button.x = x + offset.x;
					button.y = y + offset.y;
				}
				else if (element instanceof Widget){
					final Widget widget = (Widget)element;
					widget.setX(x + offset.x);
					widget.setY(y + offset.y);
				}
				
				if (element instanceof IRenderable){
					((IRenderable)element).render(mouseX, mouseY, tickDelta);
				}
			}
		}
	}
}
