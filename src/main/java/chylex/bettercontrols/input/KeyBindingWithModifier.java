package chylex.bettercontrols.input;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil.Type;
import org.jetbrains.annotations.Nullable;

public class KeyBindingWithModifier extends KeyBinding{
	public static final String CATEGORY = "key.categories.bettercontrols";
	
	public static boolean checkCategoryMatches(final String name){
		return I18n.translate(CATEGORY).equals(name);
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
