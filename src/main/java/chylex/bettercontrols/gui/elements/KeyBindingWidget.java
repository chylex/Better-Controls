package chylex.bettercontrols.gui.elements;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class KeyBindingWidget extends Button.Plain {
	private final KeyMapping binding;
	private final Component bindingName;
	
	private final List<AbstractButton> linkedButtons = new ArrayList<>(1);
	
	private final Consumer<KeyBindingWidget> onEditingStarted;
	private boolean isEditing;
	
	public KeyBindingWidget(int x, int y, int width, int height, Component bindingName, KeyMapping binding, Consumer<KeyBindingWidget> onEditingStarted) {
		super(x, y, width, height, Component.empty(), btn -> {}, DEFAULT_NARRATION);
		this.binding = binding;
		this.bindingName = bindingName;
		this.onEditingStarted = onEditingStarted;
		updateKeyBindingText();
	}
	
	public KeyBindingWidget(int x, int y, int width, Component bindingName, KeyMapping binding, Consumer<KeyBindingWidget> onEditingStarted) {
		this(x, y, width, 20, bindingName, binding, onEditingStarted);
	}
	
	public void linkButtonToBoundState(AbstractButton button) {
		linkedButtons.add(button);
		button.active = !binding.isUnbound();
	}
	
	@NotNull
	@Override
	protected MutableComponent createNarrationMessage() {
		return binding.isUnbound()
			? Component.translatable("narrator.controls.unbound", bindingName)
			: Component.translatable("narrator.controls.bound", bindingName, super.createNarrationMessage());
	}
	
	@Override
	public void onPress(@NotNull InputWithModifiers input) {
		isEditing = true;
		onEditingStarted.accept(this);
		updateKeyBindingText();
	}
	
	public void bindAndStopEditing(InputConstants.Key key) {
		binding.setKey(key);
		stopEditing();
		
		for (AbstractButton button : linkedButtons) {
			button.active = !binding.isUnbound();
		}
	}
	
	public void stopEditing() {
		isEditing = false;
		updateKeyBindingText();
	}
	
	public void updateKeyBindingText() {
		boolean hasConflict = false;
		MutableComponent conflictText = Component.empty();
		
		if (!binding.isUnbound()) {
			for (KeyMapping other : Minecraft.getInstance().options.keyMappings) {
				if (binding != other && binding.same(other)) {
					if (hasConflict) {
						conflictText.append(", ");
					}
					
					hasConflict = true;
					conflictText.append(Component.translatable(other.getName()));
				}
			}
		}
		
		if (hasConflict) {
			setMessage(getMessageWithConflict(binding));
			setTooltip(Tooltip.create(Component.translatable("controls.keybinds.duplicateKeybinds", conflictText)));
		}
		else {
			setMessage(getMessageWithoutConflict(binding));
			setTooltip(null);
		}
		
		if (isEditing) {
			setMessage(getEditingMessage(getMessage()));
		}
	}
	
	private static MutableComponent getMessageWithConflict(KeyMapping binding) {
		return Component
			.literal("[ ")
			.append(binding.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.WHITE))
			.append(" ]")
			.withStyle(ChatFormatting.YELLOW);
	}
	
	private static Component getMessageWithoutConflict(KeyMapping binding) {
		if (binding.isUnbound()) {
			return Component
				.literal("(")
				.append(Component.translatable("key.keyboard.unknown"))
				.append(")");
		}
		
		return binding.getTranslatedKeyMessage();
	}
	
	private static MutableComponent getEditingMessage(Component originalMessage) {
		return Component
			.literal("> ")
			.append(originalMessage.copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
			.append(" <")
			.withStyle(ChatFormatting.YELLOW);
	}
}
