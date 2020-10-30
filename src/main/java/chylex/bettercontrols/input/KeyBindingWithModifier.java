package chylex.bettercontrols.input;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import javax.annotation.Nullable;

public class KeyBindingWithModifier extends KeyBinding{
	public static final String CATEGORY = "key.categories.bettercontrols";
	
	public static boolean checkCategoryMatches(final String text){
		return I18n.format(CATEGORY).equals(text);
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
