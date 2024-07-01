package chylex.bettercontrols.mixin;

import chylex.bettercontrols.BetterControlsCommon;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class HookLoadGameOptions {
	@Unique
	private boolean hasLoaded = false;
	
	@Mutable
	@Final
	@Shadow
	public KeyMapping[] keyMappings;
	
	@Inject(method = "load()V", at = @At("HEAD"))
	private void load(final CallbackInfo info) {
		if (hasLoaded) {
			return;
		}
		
		final BetterControlsConfig config = BetterControlsCommon.getConfig();
		if (config == null) {
			return;
		}
		
		hasLoaded = true;
		keyMappings = ArrayUtils.addAll(keyMappings, config.getAllKeyBindings());
		AccessKeyBindingFields.getCategoryOrderMap().put(KeyBindingWithModifier.CATEGORY, Integer.valueOf(Integer.MAX_VALUE));
	}
}
