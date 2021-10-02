package chylex.bettercontrols.gui.elements;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;

public final class BooleanValueWidget extends Button {
	private final BooleanConsumer onChanged;
	private boolean value;
	
	public BooleanValueWidget(final int x, final int y, final int width, final int height, final boolean currentValue, final BooleanConsumer onChanged) {
		super(x, y, width, height, currentValue ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF, ignore -> {});
		this.value = currentValue;
		this.onChanged = onChanged;
	}
	
	public BooleanValueWidget(final int x, final int y, final int width, final boolean currentValue, final BooleanConsumer onChanged) {
		this(x, y, width, 20, currentValue, onChanged);
	}
	
	@Override
	public void onPress() {
		super.onPress();
		value = !value;
		setMessage(value ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
		onChanged.accept(value);
	}
}
