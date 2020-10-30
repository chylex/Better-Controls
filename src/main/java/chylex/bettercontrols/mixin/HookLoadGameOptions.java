package chylex.bettercontrols.mixin;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import net.minecraft.client.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public abstract class HookLoadGameOptions{
	private boolean hasLoaded = false;
	
	@Shadow
	public KeyBinding[] keyBindings;
	
	@Inject(method = "loadOptions()V", at = @At("HEAD"))
	private void load(final CallbackInfo info){
		if (hasLoaded){
			return;
		}
		
		hasLoaded = true;
		keyBindings = ArrayUtils.addAll(keyBindings, BetterControlsMod.config.getAllKeyBindings());
		AccessKeyBindingFields.getCategoryOrderMap().put(KeyBindingWithModifier.CATEGORY, Integer.valueOf(Integer.MAX_VALUE));
	}
}
