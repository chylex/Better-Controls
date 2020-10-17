package chylex.bettercontrols.mixin;
import chylex.bettercontrols.input.ToggleTrackerForStickyKey;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.options.StickyKeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.BooleanSupplier;

@Mixin(StickyKeyBinding.class)
public abstract class HookStickyKeyBindingState extends KeyBinding{
	@Shadow
	@Final
	private BooleanSupplier toggleGetter;
	
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
	public boolean isPressed(){
		return super.isPressed() || (ToggleTrackerForStickyKey.isOverrideEnabled(this) && toggleGetter.getAsBoolean());
	}
}
