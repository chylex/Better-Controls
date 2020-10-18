package chylex.bettercontrols.gui.elements;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;

public final class BooleanValueWidget extends ButtonWidget{
	private final BooleanConsumer onChanged;
	private boolean value;
	
	public BooleanValueWidget(final int x, final int y, final int width, final int height, final boolean currentValue, final BooleanConsumer onChanged){
		super(x, y, width, height, I18n.translate(currentValue ? "options.on" : "options.off"), ignore -> {});
		this.value = currentValue;
		this.onChanged = onChanged;
	}
	
	public BooleanValueWidget(final int x, final int y, final int width, final boolean currentValue, final BooleanConsumer onChanged){
		this(x, y, width, 20, currentValue, onChanged);
	}
	
	@Override
	public void onPress(){
		super.onPress();
		value = !value;
		setMessage(I18n.translate(value ? "options.on" : "options.off"));
		onChanged.accept(value);
	}
}
