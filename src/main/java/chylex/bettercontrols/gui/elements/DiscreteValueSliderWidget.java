package chylex.bettercontrols.gui.elements;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.util.Mth;
import java.util.List;
import java.util.function.Consumer;

public final class DiscreteValueSliderWidget<T> extends AbstractSliderButton{
	private final List<Option<T>> options;
	private final Consumer<T> onChanged;
	private T selectedValue;
	
	public DiscreteValueSliderWidget(final int x, final int y, final int width, final int height, final List<Option<T>> options, final T selectedValue, final Consumer<T> onChanged){
		super(x, y, width, height, Option.find(options, selectedValue).getText(), options.indexOf(Option.find(options, selectedValue)) / (options.size() - 1.0));
		this.options = options;
		this.selectedValue = selectedValue;
		this.onChanged = onChanged;
	}
	
	public DiscreteValueSliderWidget(final int x, final int y, final int width, final List<Option<T>> options, final T selectedValue, final Consumer<T> onChanged){
		this(x, y, width, 20, options, selectedValue, onChanged);
	}
	
	public Option<T> getSelectedOption(){
		return options.get(Mth.floor(Mth.clampedLerp(0.0, options.size() - 1.0, value)));
	}
	
	@Override
	protected void updateMessage(){
		setMessage(getSelectedOption().getText());
	}
	
	@Override
	protected void applyValue(){
		final T newSelectedValue = getSelectedOption().getValue();
		
		if (selectedValue != newSelectedValue){
			selectedValue = newSelectedValue;
			onChanged.accept(newSelectedValue);
		}
	}
}
