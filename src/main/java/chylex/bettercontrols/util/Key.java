package chylex.bettercontrols.util;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public final class Key {
	private Key() {}
	
	public static final InputConstants.Key INVALID = InputConstants.UNKNOWN;
	
	public static boolean isUnbound(final KeyMapping binding) {
		return binding.isUnbound();
	}
	
	public static boolean isPressed(final KeyMapping binding) {
		return binding.isDown();
	}
	
	public static boolean wasPressed(final KeyMapping binding) {
		return binding.consumeClick();
	}
	
	public static Component getBoundKeyText(final KeyMapping binding) {
		return binding.getTranslatedKeyMessage();
	}
	
	public static void bind(final KeyMapping binding, final InputConstants.Key input) {
		binding.setKey(input);
	}
	
	public static String writeBinding(final KeyMapping binding) {
		return binding.saveString();
	}
	
	public static void readBinding(final KeyMapping binding, final String serialized) {
		bind(binding, InputConstants.getKey(serialized));
	}
	
	public static InputConstants.Key inputFromMouse(final int button) {
		return InputConstants.Type.MOUSE.getOrCreate(button);
	}
	
	public static InputConstants.Key inputFromKeyboard(final int keyCode, final int scanCode) {
		return InputConstants.getKey(keyCode, scanCode);
	}
}
