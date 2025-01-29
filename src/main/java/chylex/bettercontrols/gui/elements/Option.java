package chylex.bettercontrols.gui.elements;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public record Option<T>(T value, Component text) {
	public static <T> Option<T> find(List<Option<T>> options, T value) {
		return options.stream().filter(it -> Objects.equals(it.value, value)).findFirst().orElseGet(() -> options.get(0));
	}
	
	public static <T> CycleButton<Option<T>> button(int x, int y, int width, Component text, List<Option<T>> options, T initialValue, Consumer<T> onValueChanged) {
		return CycleButton.<Option<T>>builder(Option::text)
			.displayOnlyValue()
			.withValues(options)
			.withInitialValue(find(options, initialValue))
			.create(x, y, width, 20, text, (btn, newValue) -> onValueChanged.accept(newValue.value()));
	}
}
