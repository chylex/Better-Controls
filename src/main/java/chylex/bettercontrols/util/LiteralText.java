package chylex.bettercontrols.util;
import net.minecraft.network.chat.TextComponent;

public final class LiteralText extends TextComponent {
	public static LiteralText text(final String text) {
		return new LiteralText(text);
	}
	
	public LiteralText(final String msg) {
		super(msg);
	}
}
