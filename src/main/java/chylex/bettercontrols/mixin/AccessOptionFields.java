package chylex.bettercontrols.mixin;

import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Option.class)
public interface AccessOptionFields {
	@Accessor
	Text getKey();
}
