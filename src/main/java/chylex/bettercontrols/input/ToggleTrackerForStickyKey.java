package chylex.bettercontrols.input;
import chylex.bettercontrols.mixin.AccessKeyBindingFields;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.KeyMapping;
import java.util.HashSet;
import java.util.Set;

public final class ToggleTrackerForStickyKey extends ToggleTracker {
	private static final Set<KeyMapping> enabledOverrides = new HashSet<>();
	
	public static boolean isOverrideEnabled(final KeyMapping binding) {
		return enabledOverrides.contains(binding);
	}
	
	private final BooleanConsumer setToggleState;
	
	public ToggleTrackerForStickyKey(final KeyMapping bindingToggle, final KeyMapping bindingStickyReset, final BooleanConsumer setToggleState) {
		super(bindingToggle, bindingStickyReset);
		this.setToggleState = setToggleState;
		this.setToggleState.accept(false);
		enabledOverrides.add(bindingStickyReset);
	}
	
	@Override
	public boolean tick() {
		final boolean isToggled = super.tick();
		setToggleState.accept(isToggled);
		return isToggled;
	}
	
	@Override
	protected boolean isResetKeyPressed() {
		return ((AccessKeyBindingFields)bindingReset).isPressedField();
	}
}
