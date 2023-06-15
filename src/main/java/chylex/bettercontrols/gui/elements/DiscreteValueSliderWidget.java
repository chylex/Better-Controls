package chylex.bettercontrols.gui.elements;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

public final class DiscreteValueSliderWidget<T> extends AbstractSliderButton {
	private final Component narration;
	private final ImmutableList<Option<T>> options;
	private final Consumer<T> onChanged;
	private T selectedValue;
	
	public DiscreteValueSliderWidget(final int x, final int y, final int width, final int height, final Component narration, final ImmutableList<Option<T>> options, final T selectedValue, final Consumer<T> onChanged) {
		super(x, y, width, height, Option.find(options, selectedValue).text(), options.indexOf(Option.find(options, selectedValue)) / (options.size() - 1.0));
		this.narration = narration;
		this.options = options;
		this.selectedValue = selectedValue;
		this.onChanged = onChanged;
	}
	
	public DiscreteValueSliderWidget(final int x, final int y, final int width, final Component narration, final ImmutableList<Option<T>> options, final T selectedValue, final Consumer<T> onChanged) {
		this(x, y, width, 20, narration, options, selectedValue, onChanged);
	}
	
	public Option<T> getSelectedOption() {
		return options.get(Mth.floor(Mth.clampedLerp(0.0, options.size() - 1.0, value)));
	}
	
	@Override
	protected void updateMessage() {
		setMessage(getSelectedOption().text());
	}
	
	@Override
	protected void applyValue() {
		final T newSelectedValue = getSelectedOption().value();
		
		if (selectedValue != newSelectedValue) {
			selectedValue = newSelectedValue;
			onChanged.accept(newSelectedValue);
		}
	}
	
	@NotNull
	@Override
	protected MutableComponent createNarrationMessage() {
		return Component.translatable("gui.narrate.slider", narration.plainCopy().append(" ").append(getMessage()));
	}
}
