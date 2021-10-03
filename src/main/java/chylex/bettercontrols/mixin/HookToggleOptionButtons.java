package chylex.bettercontrols.mixin;

import net.minecraft.client.CycleOption;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CycleOption.class)
public abstract class HookToggleOptionButtons {
	@Inject(method = "createButton", at = @At("RETURN"))
	private void disableToggleOptions(final Options options, final int x, final int y, final int width, final CallbackInfoReturnable<AbstractWidget> ci) {
		@SuppressWarnings("ConstantConditions")
		final CycleOption<?> me = (CycleOption<?>)(Object)this;
		
		if (me == Option.TOGGLE_CROUCH || me == Option.TOGGLE_SPRINT) {
			ci.getReturnValue().active = false;
		}
	}
}
