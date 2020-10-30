package chylex.bettercontrols.gui.elements;
import chylex.bettercontrols.util.Key;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.OPTIONS;

public final class KeyBindingWidget extends ButtonWidget{
	private final KeyBinding binding;
	private final String bindingName;
	
	private final List<AbstractButtonWidget> linkedButtons = new ArrayList<>(1);
	
	private final Consumer<KeyBindingWidget> onEditingStarted;
	private boolean isEditing;
	
	public KeyBindingWidget(final int x, final int y, final int width, final int height, final KeyBinding binding, final Consumer<KeyBindingWidget> onEditingStarted){
		super(x, y, width, height, "", btn -> {});
		this.binding = binding;
		this.bindingName = I18n.translate(binding.getId());
		this.onEditingStarted = onEditingStarted;
		updateKeyBindingText();
	}
	
	public KeyBindingWidget(final int x, final int y, final int width, final KeyBinding binding, final Consumer<KeyBindingWidget> onEditingStarted){
		this(x, y, width, 20, binding, onEditingStarted);
	}
	
	public void linkButtonToBoundState(final AbstractButtonWidget button){
		linkedButtons.add(button);
		button.active = !Key.isUnbound(binding);
	}
	
	@Override
	protected String getNarrationMessage(){
		return Key.isUnbound(binding) ? I18n.translate("narrator.controls.unbound", bindingName) : I18n.translate("narrator.controls.bound", bindingName, super.getNarrationMessage());
	}
	
	@Override
	public void onPress(){
		isEditing = true;
		onEditingStarted.accept(this);
		updateKeyBindingText();
	}
	
	public void bindAndStopEditing(final InputUtil.KeyCode key){
		Key.bind(binding, key);
		stopEditing();
		
		for(final AbstractButtonWidget button : linkedButtons){
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
			for(final KeyBinding other : OPTIONS.keysAll){
				if (binding != other && binding.equals(other)){
					hasConflict = true;
					break;
				}
			}
		}
		
		if (isEditing){
			setMessage(Formatting.WHITE + "> " + Formatting.YELLOW + binding.getLocalizedName() + Formatting.WHITE + " <");
		}
		else if (hasConflict){
			setMessage(Formatting.RED + Key.getBoundKeyText(binding));
		}
		else{
			setMessage(Key.isUnbound(binding) ? "(No Binding)" : Key.getBoundKeyText(binding));
		}
	}
}
