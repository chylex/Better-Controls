package chylex.bettercontrols.input;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class KeyBindingWithModifier extends KeyBinding{
	public static final String CATEGORY = "key.categories.bettercontrols";
	
	public static boolean checkCategoryMatches(final Text text){
		return text instanceof TranslatableText && CATEGORY.equals(((TranslatableText)text).getKey());
	}
	
	@Nullable
	private ModifierKey modifier = null;
	
	public KeyBindingWithModifier(final String translationKey){
		super(translationKey, Type.KEYSYM, -1, CATEGORY);
	}
	
	public void setModifier(@Nullable final ModifierKey modifier){
		this.modifier = modifier;
	}
	
	@Nullable
	public ModifierKey getModifier(){
		return modifier;
	}
	
	@Override
	public boolean isPressed(){
		return super.isPressed() && (modifier == null || modifier.isPressed());
	}
	
	@Override
	public boolean wasPressed(){
		return super.wasPressed() && (modifier == null || modifier.isPressed());
	}
}
