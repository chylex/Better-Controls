package chylex.bettercontrols.util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

public final class Statics {
	private Statics() {}
	
	public static final MinecraftClient MINECRAFT = MinecraftClient.getInstance();
	public static final GameOptions OPTIONS = MINECRAFT.options;
	
	public static final KeyBinding KEY_SPRINT = OPTIONS.keySprint;
	public static final KeyBinding KEY_SNEAK = OPTIONS.keySneak;
	public static final KeyBinding KEY_FORWARD = OPTIONS.keyForward;
	public static final KeyBinding KEY_JUMP = OPTIONS.keyJump;
}
