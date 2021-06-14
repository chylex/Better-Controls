package chylex.bettercontrols.gui.elements;
import chylex.bettercontrols.util.LiteralText;
import java.util.List;
import java.util.Objects;

public final class Option<T> {
	private final T value;
	private final LiteralText text;
	
	public Option(final T value, final LiteralText text) {
		this.value = value;
		this.text = text;
	}
	
	public T getValue() {
		return value;
	}
	
	public LiteralText getText() {
		return text;
	}
	
	public static <T> Option<T> find(final List<Option<T>> options, final T value) {
		return options.stream().filter(it -> Objects.equals(it.value, value)).findFirst().orElseGet(() -> options.get(0));
	}
}
