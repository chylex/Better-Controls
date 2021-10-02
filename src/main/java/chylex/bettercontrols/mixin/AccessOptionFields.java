package chylex.bettercontrols.mixin;

import net.minecraft.client.Option;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Option.class)
public interface AccessOptionFields {
	@Accessor("caption")
	Component getCaption();
}
