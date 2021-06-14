package chylex.bettercontrols.mixin;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class HookLoadGameOptions {
	private boolean hasLoaded = false;
	
	@Mutable
	@Final
	@Shadow
	public KeyBinding[] keysAll;
	
	@Inject(method = "load()V", at = @At("HEAD"))
	private void load(final CallbackInfo info) {
		if (hasLoaded) {
			return;
		}
		
		hasLoaded = true;
		keysAll = ArrayUtils.addAll(keysAll, BetterControlsMod.config.getAllKeyBindings());
		AccessKeyBindingFields.getCategoryOrderMap().put(KeyBindingWithModifier.CATEGORY, Integer.valueOf(Integer.MAX_VALUE));
	}
}
