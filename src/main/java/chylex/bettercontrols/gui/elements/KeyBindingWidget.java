package chylex.bettercontrols.gui.elements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
		button.active = !binding.isNotBound();
	}
	
	@Override
	protected String getNarrationMessage(){
		return binding.isNotBound() ? I18n.translate("narrator.controls.unbound", bindingName) : I18n.translate("narrator.controls.bound", bindingName, super.getNarrationMessage());
	}
	
	@Override
	public void onPress(){
		isEditing = true;
		onEditingStarted.accept(this);
		updateKeyBindingText();
	}
	
	public void bindAndStopEditing(final InputUtil.KeyCode key){
		binding.setKeyCode(key);
		stopEditing();
		
		for(final AbstractButtonWidget button : linkedButtons){
			button.active = !binding.isNotBound();
		}
	}
	
	public void stopEditing(){
		isEditing = false;
		updateKeyBindingText();
	}
	
	public void updateKeyBindingText(){
		boolean hasConflict = false;
		
		if (!binding.isNotBound()){
			for(final KeyBinding other : MinecraftClient.getInstance().options.keysAll){
				if (binding != other && binding.equals(other)){
					hasConflict = true;
				}
			}
		}
		
		if (isEditing){
			setMessage(Formatting.WHITE + "> " + Formatting.YELLOW + binding.getLocalizedName() + Formatting.WHITE + " <");
		}
		else if (hasConflict){
			setMessage(Formatting.RED + binding.getLocalizedName());
		}
		else{
			setMessage(binding.isNotBound() ? "(No Binding)" : binding.getLocalizedName());
		}
	}
}
