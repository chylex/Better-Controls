package chylex.bettercontrols.gui.elements;
import net.minecraft.client.gui.widget.ButtonWidget;
import java.util.List;
import java.util.function.Consumer;

public class CycleButtonWidget<T> extends ButtonWidget {
	private final List<Option<T>> options;
	private final Consumer<T> onChanged;
	private T selectedValue;
	
	public CycleButtonWidget(final int x, final int y, final int width, final int height, final List<Option<T>> options, final T selectedValue, final Consumer<T> onChanged) {
		super(x, y, width, height, Option.find(options, selectedValue).getText(), btn -> {});
		this.options = options;
		this.selectedValue = selectedValue;
		this.onChanged = onChanged;
	}
	
	public CycleButtonWidget(final int x, final int y, final int width, final List<Option<T>> options, final T selectedValue, final Consumer<T> onChanged) {
		this(x, y, width, 20, options, selectedValue, onChanged);
	}
	
	@Override
	public void onPress() {
		int nextIndex = options.indexOf(Option.find(options, selectedValue)) + 1;
		
		if (nextIndex >= options.size()) {
			nextIndex = 0;
		}
		
		final Option<T> newSelectedOption = options.get(nextIndex);
		
		selectedValue = newSelectedOption.getValue();
		onChanged.accept(selectedValue);
		setMessage(newSelectedOption.getText());
	}
}
