package chylex.bettercontrols.input;

import chylex.bettercontrols.Mixins;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.KeyMapping;
import java.util.HashSet;
import java.util.Set;

public final class ToggleTrackerForStickyKey extends ToggleTracker {
	private static final Set<KeyMapping> enabledOverrides = new HashSet<>();
	
	public static boolean isOverrideEnabled(KeyMapping binding) {
		return enabledOverrides.contains(binding);
	}
	
	private final BooleanConsumer setToggleState;
	
	public ToggleTrackerForStickyKey(KeyMapping bindingToggle, KeyMapping bindingStickyReset, BooleanConsumer setToggleState) {
		super(bindingToggle, bindingStickyReset);
		this.setToggleState = setToggleState;
		this.setToggleState.accept(false);
		enabledOverrides.add(bindingStickyReset);
	}
	
	@Override
	public boolean tick() {
		boolean isToggled = super.tick();
		setToggleState.accept(isToggled);
		return isToggled;
	}
	
	@Override
	protected boolean isResetKeyPressed() {
		return Mixins.keyMappingFields(bindingReset).isPressedField();
	}
}
