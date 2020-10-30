package chylex.bettercontrols.util;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public final class Statics{
	private Statics(){}
	
	public static final Minecraft MINECRAFT = Minecraft.getInstance();
	public static final GameSettings OPTIONS = MINECRAFT.gameSettings;
	
	public static final KeyBinding KEY_SPRINT = OPTIONS.keyBindSprint;
	public static final KeyBinding KEY_SNEAK = OPTIONS.keyBindSneak;
	public static final KeyBinding KEY_FORWARD = OPTIONS.keyBindForward;
	public static final KeyBinding KEY_JUMP = OPTIONS.keyBindJump;
}
