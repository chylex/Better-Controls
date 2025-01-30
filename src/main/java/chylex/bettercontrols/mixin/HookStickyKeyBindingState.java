package chylex.bettercontrols.mixin;

import chylex.bettercontrols.Mixins;
import chylex.bettercontrols.input.ToggleTrackerForStickyKey;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.BooleanSupplier;

@Mixin(ToggleKeyMapping.class)
public abstract class HookStickyKeyBindingState extends KeyMapping {
	@Shadow
	@Final
	private BooleanSupplier needsToggle;
	
	public HookStickyKeyBindingState(String translationKey, int code, String category) {
		super(translationKey, code, category);
	}
	
	@Inject(method = "setDown", at = @At("HEAD"), cancellable = true)
	public void setPressed(boolean pressed, CallbackInfo info) {
		if (ToggleTrackerForStickyKey.isOverrideEnabled(this)) {
			Mixins.keyMappingFields(this).setPressedField(pressed);
			info.cancel();
		}
	}
	
	@Override
	public boolean isDown() {
		return super.isDown() || (ToggleTrackerForStickyKey.isOverrideEnabled(this) && needsToggle.getAsBoolean());
	}
}
