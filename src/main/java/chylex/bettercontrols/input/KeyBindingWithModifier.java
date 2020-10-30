package chylex.bettercontrols.input;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import javax.annotation.Nullable;

public class KeyBindingWithModifier extends KeyBinding{
	public static final String CATEGORY = "key.categories.bettercontrols";
	
	public static boolean checkCategoryMatches(final ITextComponent text){
		return text instanceof TranslationTextComponent && CATEGORY.equals(((TranslationTextComponent)text).getKey());
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
	public boolean isKeyDown(){
		return super.isKeyDown() && (modifier == null || modifier.isPressed());
	}
	
	@Override
	public boolean isPressed(){
		return super.isPressed() && (modifier == null || modifier.isPressed());
	}
}
