package chylex.bettercontrols.input;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class KeyBindingWithModifier extends KeyMapping {
	@SuppressWarnings("SpellCheckingInspection")
	public static final Category CATEGORY = new Category(Identifier.fromNamespaceAndPath("bettercontrols", "all"));
	
	@Nullable
	private ModifierKey modifier;
	
	public KeyBindingWithModifier(String translationKey) {
		super(translationKey, Type.KEYSYM, -1, CATEGORY);
	}
	
	public void setModifier(@Nullable ModifierKey modifier) {
		this.modifier = modifier;
	}
	
	@Nullable
	public ModifierKey getModifier() {
		return modifier;
	}
	
	@Override
	public boolean isDown() {
		return super.isDown() && (modifier == null || modifier.isPressed());
	}
	
	@Override
	public boolean consumeClick() {
		return super.consumeClick() && (modifier == null || modifier.isPressed());
	}
}
