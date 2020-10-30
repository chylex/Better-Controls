package chylex.bettercontrols.util;
import net.minecraft.util.text.StringTextComponent;

public final class LiteralText extends StringTextComponent{
	public static LiteralText text(final String text){
		return new LiteralText(text);
	}
	
	private final String msg;
	
	public LiteralText(final String msg){
		super(msg);
		this.msg = msg;
	}
	
	public LiteralText copy(){
		return new LiteralText(msg);
	}
}
