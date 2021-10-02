package chylex.bettercontrols.util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public final class Statics{
	private Statics(){}
	
	public static final Minecraft MINECRAFT = Minecraft.getInstance();
	public static final Options OPTIONS = MINECRAFT.options;
	
	public static final KeyMapping KEY_SPRINT = OPTIONS.keySprint;
	public static final KeyMapping KEY_SNEAK = OPTIONS.keyShift;
	public static final KeyMapping KEY_FORWARD = OPTIONS.keyUp;
	public static final KeyMapping KEY_JUMP = OPTIONS.keyJump;
}
