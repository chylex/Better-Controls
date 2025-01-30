package chylex.bettercontrols.input;

import chylex.bettercontrols.Mixins;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.OptionInstance;
import java.util.HashSet;
import java.util.Set;

public final class ToggleTrackerForStickyKey extends ToggleTracker {
	private static final Set<KeyMapping> enabledOverrides = new HashSet<>();
	
	@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
	public static boolean isOverrideEnabled(KeyMapping binding) {
		return enabledOverrides.contains(binding);
	}
	
	private final OptionInstance<Boolean> toggleOption;
	
	public ToggleTrackerForStickyKey(KeyMapping bindingToggle, KeyMapping bindingStickyReset, OptionInstance<Boolean> toggleOption) {
		super(bindingToggle, bindingStickyReset, toggleOption.get().booleanValue());
		this.toggleOption = toggleOption;
		enabledOverrides.add(bindingStickyReset);
	}
	
	@Override
	public boolean tick() {
		boolean isToggled = super.tick();
		toggleOption.set(Boolean.valueOf(isToggled));
		return isToggled;
	}
	
	@Override
	protected boolean isResetKeyPressed() {
		return Mixins.keyMappingFields(bindingReset).isPressedField();
	}
}
