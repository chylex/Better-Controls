package chylex.bettercontrols.util;

public final class LiteralText extends net.minecraft.text.LiteralText{
	public static LiteralText text(final String text){
		return new LiteralText(text);
	}
	
	public LiteralText(final String msg){
		super(msg);
	}
}
