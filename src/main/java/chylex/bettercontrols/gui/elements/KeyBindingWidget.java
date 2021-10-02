package chylex.bettercontrols.gui.elements;
import chylex.bettercontrols.util.Key;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static chylex.bettercontrols.util.Statics.OPTIONS;

public final class KeyBindingWidget extends Button {
	private final KeyMapping binding;
	private final Component bindingName;
	
	private final List<AbstractButton> linkedButtons = new ArrayList<>(1);
	
	private final Consumer<KeyBindingWidget> onEditingStarted;
	private boolean isEditing;
	
	public KeyBindingWidget(final int x, final int y, final int width, final int height, final KeyMapping binding, final Consumer<KeyBindingWidget> onEditingStarted) {
		super(x, y, width, height, TextComponent.EMPTY, btn -> {});
		this.binding = binding;
		this.bindingName = new TranslatableComponent(binding.saveString());
		this.onEditingStarted = onEditingStarted;
		updateKeyBindingText();
	}
	
	public KeyBindingWidget(final int x, final int y, final int width, final KeyMapping binding, final Consumer<KeyBindingWidget> onEditingStarted) {
		this(x, y, width, 20, binding, onEditingStarted);
	}
	
	public void linkButtonToBoundState(final AbstractButton button) {
		linkedButtons.add(button);
		button.active = !Key.isUnbound(binding);
	}
	
	@Override
	protected MutableComponent createNarrationMessage() {
		return Key.isUnbound(binding) ? new TranslatableComponent("narrator.controls.unbound", bindingName) : new TranslatableComponent("narrator.controls.bound", bindingName, super.createNarrationMessage());
	}
	
	@Override
	public void onPress() {
		isEditing = true;
		onEditingStarted.accept(this);
		updateKeyBindingText();
	}
	
	public void bindAndStopEditing(final InputConstants.Key key) {
		Key.bind(binding, key);
		stopEditing();
		
		for (final AbstractButton button : linkedButtons) {
			button.active = !Key.isUnbound(binding);
		}
	}
	
	public void stopEditing() {
		isEditing = false;
		updateKeyBindingText();
	}
	
	public void updateKeyBindingText() {
		boolean hasConflict = false;
		
		if (!Key.isUnbound(binding)) {
			for (final KeyMapping other : OPTIONS.keyMappings) {
				if (binding != other && binding.equals(other)) {
					hasConflict = true;
					break;
				}
			}
		}
		
		if (isEditing) {
			setMessage((new TextComponent("> ")).append(Key.getBoundKeyText(binding).copy().withStyle(ChatFormatting.YELLOW)).append(" <").withStyle(ChatFormatting.YELLOW));
		}
		else if (hasConflict) {
			setMessage(Key.getBoundKeyText(binding).copy().withStyle(ChatFormatting.RED));
		}
		else {
			setMessage(Key.isUnbound(binding) ? new TextComponent("(No Binding)") : Key.getBoundKeyText(binding));
		}
	}
}
