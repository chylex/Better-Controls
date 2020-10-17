package chylex.bettercontrols.input;
import chylex.bettercontrols.mixin.AccessKeyBindingFields;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.options.KeyBinding;
import java.util.HashSet;
import java.util.Set;

public final class ToggleTrackerForStickyKey extends ToggleTracker{
	private static final Set<KeyBinding> enabledOverrides = new HashSet<>();
	
	public static boolean isOverrideEnabled(final KeyBinding binding){
		return enabledOverrides.contains(binding);
	}
	
	private final BooleanConsumer setToggleState;
	
	public ToggleTrackerForStickyKey(final KeyBinding bindingToggle, final KeyBinding bindingStickyReset, final BooleanConsumer setToggleState){
		super(bindingToggle, bindingStickyReset);
		this.setToggleState = setToggleState;
		this.setToggleState.accept(false);
		enabledOverrides.add(bindingStickyReset);
	}
	
	@Override
	public boolean tick(){
		final boolean isToggled = super.tick();
		setToggleState.accept(isToggled);
		return isToggled;
	}
	
	@Override
	protected boolean isResetKeyPressed(){
		return ((AccessKeyBindingFields)bindingReset).isPressedField();
	}
}
