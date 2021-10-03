package chylex.bettercontrols.gui.elements;
import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.Objects;

public record Option<T>(T value, Component text) {
	public static <T> Option<T> find(final List<Option<T>> options, final T value) {
		return options.stream().filter(it -> Objects.equals(it.value, value)).findFirst().orElseGet(() -> options.get(0));
	}
}
