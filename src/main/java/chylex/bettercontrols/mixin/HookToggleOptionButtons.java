package chylex.bettercontrols.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.function.Consumer;

@Mixin(OptionInstance.class)
public abstract class HookToggleOptionButtons {
	@Inject(method = "createButton(Lnet/minecraft/client/Options;IIILjava/util/function/Consumer;)Lnet/minecraft/client/gui/components/AbstractWidget;", at = @At("RETURN"))
	private <T> void disableToggleOptions(final Options options, final int x, final int y, final int width, final Consumer<T> callback, final CallbackInfoReturnable<AbstractWidget> cir) {
		@SuppressWarnings("ConstantConditions")
		final OptionInstance<?> me = (OptionInstance<?>)(Object)this;
		
		if (me == options.toggleCrouch() || me == options.toggleSprint()) {
			cir.getReturnValue().active = false;
		}
	}
}
