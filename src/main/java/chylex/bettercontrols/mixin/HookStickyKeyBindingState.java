package chylex.bettercontrols.mixin;
import chylex.bettercontrols.input.ToggleTrackerForStickyKey;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.ToggleableKeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.BooleanSupplier;

@Mixin(ToggleableKeyBinding.class)
public abstract class HookStickyKeyBindingState extends KeyBinding{
	@Shadow
	@Final
	private BooleanSupplier getterToggle;
	
	public HookStickyKeyBindingState(final String translationKey, final int code, final String category){
		super(translationKey, code, category);
	}
	
	@Inject(method = "setPressed(Z)V", at = @At("HEAD"), cancellable = true)
	public void setPressed(final boolean pressed, final CallbackInfo info){
		if (ToggleTrackerForStickyKey.isOverrideEnabled(this)){
			((AccessKeyBindingFields)this).setPressedField(pressed);
			info.cancel();
		}
	}
	
	@Override
	public boolean isKeyDown(){
		return super.isKeyDown() || (ToggleTrackerForStickyKey.isOverrideEnabled(this) && getterToggle.getAsBoolean());
	}
}
