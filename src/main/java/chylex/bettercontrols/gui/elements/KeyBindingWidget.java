package chylex.bettercontrols.gui.elements;
import chylex.bettercontrols.util.Key;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.OPTIONS;

public final class KeyBindingWidget extends Button{
	private final KeyBinding binding;
	private final ITextComponent bindingName;
	
	private final List<AbstractButton> linkedButtons = new ArrayList<>(1);
	
	private final Consumer<KeyBindingWidget> onEditingStarted;
	private boolean isEditing;
	
	public KeyBindingWidget(final int x, final int y, final int width, final int height, final KeyBinding binding, final Consumer<KeyBindingWidget> onEditingStarted){
		super(x, y, width, height, "", btn -> {});
		this.binding = binding;
		this.bindingName = new TranslationTextComponent(binding.getTranslationKey());
		this.onEditingStarted = onEditingStarted;
		updateKeyBindingText();
	}
	
	public KeyBindingWidget(final int x, final int y, final int width, final KeyBinding binding, final Consumer<KeyBindingWidget> onEditingStarted){
		this(x, y, width, 20, binding, onEditingStarted);
	}
	
	public void linkButtonToBoundState(final AbstractButton button){
		linkedButtons.add(button);
		button.active = !Key.isUnbound(binding);
	}
	
	@Override
	protected String getNarrationMessage(){
		return Key.isUnbound(binding) ? I18n.format("narrator.controls.unbound", bindingName) : I18n.format("narrator.controls.bound", bindingName, super.getNarrationMessage());
	}
	
	@Override
	public void onPress(){
		isEditing = true;
		onEditingStarted.accept(this);
		updateKeyBindingText();
	}
	
	public void bindAndStopEditing(final InputMappings.Input key){
		Key.bind(binding, key);
		stopEditing();
		
		for(final AbstractButton button : linkedButtons){
			button.active = !Key.isUnbound(binding);
		}
	}
	
	public void stopEditing(){
		isEditing = false;
		updateKeyBindingText();
	}
	
	public void updateKeyBindingText(){
		boolean hasConflict = false;
		
		if (!Key.isUnbound(binding)){
			for(final KeyBinding other : OPTIONS.keyBindings){
				if (binding != other && binding.equals(other)){
					hasConflict = true;
					break;
				}
			}
		}
		
		if (isEditing){
			setMessage(TextFormatting.WHITE + "> " + TextFormatting.YELLOW + Key.getBoundKeyText(binding) + TextFormatting.WHITE + " <");
		}
		else if (hasConflict){
			setMessage(TextFormatting.RED + Key.getBoundKeyText(binding));
		}
		else{
			setMessage(Key.isUnbound(binding) ? "(No Binding)" : Key.getBoundKeyText(binding));
		}
	}
}
