package chylex.bettercontrols.gui;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.elements.BooleanValueWidget;
import chylex.bettercontrols.gui.elements.CycleButtonWidget;
import chylex.bettercontrols.gui.elements.DiscreteValueSliderWidget;
import chylex.bettercontrols.gui.elements.KeyBindingWidget;
import chylex.bettercontrols.gui.elements.Option;
import chylex.bettercontrols.gui.elements.TextWidget;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import chylex.bettercontrols.input.ModifierKey;
import chylex.bettercontrols.input.SprintMode;
import chylex.bettercontrols.util.Key;
import chylex.bettercontrols.util.LiteralText;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.glfw.GLFW;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static chylex.bettercontrols.gui.OptionListWidget.COL2_W;
import static chylex.bettercontrols.gui.OptionListWidget.COL4_W;
import static chylex.bettercontrols.gui.OptionListWidget.ROW_WIDTH;
import static chylex.bettercontrols.gui.OptionListWidget.col2;
import static chylex.bettercontrols.gui.OptionListWidget.col4;
import static chylex.bettercontrols.gui.elements.TextWidget.CENTER;
import static chylex.bettercontrols.util.LiteralText.text;
import static chylex.bettercontrols.util.Statics.OPTIONS;

public class BetterControlsScreen extends SettingsScreen{
	public static final LiteralText TITLE = text("Better Controls");
	
	private static final int BOTTOM_PADDING = 3;
	private static final int TEXT_PADDING_RIGHT = 4;
	private static final int TITLE_MARGIN_TOP = 3;
	private static final int ROW_HEIGHT = 22;
	
	private final List<Option<SprintMode>> SPRINT_MODE_OPTIONS = Arrays.asList(
		new Option<>(SprintMode.TAP_TO_START, text("Tap To Start Sprinting")),
		new Option<>(SprintMode.TAP_TO_TOGGLE, text("Tap To Start / Stop Sprinting")),
		new Option<>(SprintMode.HOLD, text("Hold To Sprint"))
	);
	
	// Options
	
	private int generateSprintingOptions(int y, final List<IGuiEventListener> elements){
		final BetterControlsConfig cfg = BetterControlsMod.config;
		
		generateKeyBindingWithModifierOption(y, elements, text("Toggle Sprint"), cfg.keyToggleSprint);
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Sprint Key Mode"));
		elements.add(new CycleButtonWidget<>(col2(1), y, COL2_W, SPRINT_MODE_OPTIONS, cfg.sprintMode, value -> cfg.sprintMode = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Double Tap 'Walk Forwards' To Sprint"));
		elements.add(new BooleanValueWidget(col2(1), y, COL2_W, cfg.doubleTapForwardToSprint, value -> cfg.doubleTapForwardToSprint = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Resume Sprinting After Hitting Obstacle"));
		elements.add(new BooleanValueWidget(col2(1), y, COL2_W, cfg.resumeSprintingAfterHittingObstacle, value -> cfg.resumeSprintingAfterHittingObstacle = value));
		
		y += ROW_HEIGHT;
		return y;
	}
	
	private int generateSneakingOptions(int y, final List<IGuiEventListener> elements){
		final BetterControlsConfig cfg = BetterControlsMod.config;
		
		generateKeyBindingWithModifierOption(y, elements, text("Toggle Sneak"), cfg.keyToggleSneak);
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Move Camera Smoothly"));
		elements.add(new BooleanValueWidget(col2(1), y, COL2_W, cfg.sneakingMovesCameraSmoothly, value -> cfg.sneakingMovesCameraSmoothly = value));
		
		y += ROW_HEIGHT;
		return y;
	}
	
	@SuppressWarnings({ "AutoBoxing", "AutoUnboxing" })
	private int generateFlightOptions(int y, final List<IGuiEventListener> elements){
		final BetterControlsConfig cfg = BetterControlsMod.config;
		
		final List<Option<Float>> flightSpeedOptions = Arrays.asList(
			new Option<>(Float.valueOf(0.25F), text("0.25x")),
			new Option<>(Float.valueOf(0.50F), text("0.5x")),
			new Option<>(Float.valueOf(0.75F), text("0.75x")),
			new Option<>(Float.valueOf(1.00F), text("1x")),
			new Option<>(Float.valueOf(1.50F), text("1.5x")),
			new Option<>(Float.valueOf(2.00F), text("2x")),
			new Option<>(Float.valueOf(3.00F), text("3x")),
			new Option<>(Float.valueOf(4.00F), text("4x")),
			new Option<>(Float.valueOf(5.00F), text("5x")),
			new Option<>(Float.valueOf(6.00F), text("6x")),
			new Option<>(Float.valueOf(7.00F), text("7x")),
			new Option<>(Float.valueOf(8.00F), text("8x"))
		);
		
		final List<Option<Float>> flightVerticalBoostOptions = Arrays.asList(
			new Option<>(Float.valueOf(0.00F), text("None")),
			new Option<>(Float.valueOf(0.25F), text("+25%")),
			new Option<>(Float.valueOf(0.50F), text("+50%")),
			new Option<>(Float.valueOf(0.75F), text("+75%")),
			new Option<>(Float.valueOf(1.00F), text("+100%")),
			new Option<>(Float.valueOf(1.50F), text("+150%")),
			new Option<>(Float.valueOf(2.00F), text("+200%")),
			new Option<>(Float.valueOf(2.50F), text("+250%")),
			new Option<>(Float.valueOf(3.00F), text("+300%"))
		);
		
		generateKeyBindingWithModifierOption(y, elements, text("Toggle Flight (Creative)"), cfg.keyToggleFlight);
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Sprint Key Mode While Flying"));
		elements.add(new CycleButtonWidget<>(col2(1), y, COL2_W, SPRINT_MODE_OPTIONS, cfg.sprintModeWhileFlying, value -> cfg.sprintModeWhileFlying = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Disable Field Of View Changing"));
		elements.add(new BooleanValueWidget(col2(1), y, COL2_W, cfg.disableChangingFovWhileFlying, value -> cfg.disableChangingFovWhileFlying = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Fly On Ground (Creative Mode)"));
		elements.add(new BooleanValueWidget(col2(1), y, COL2_W, cfg.flyOnGroundInCreative, value -> cfg.flyOnGroundInCreative = value));
		
		y += ROW_HEIGHT * 4 / 3;
		
		elements.add(new TextWidget(col4(2), y, COL4_W - TEXT_PADDING_RIGHT, text("Creative"), CENTER));
		elements.add(new TextWidget(col4(3), y, COL4_W - TEXT_PADDING_RIGHT, text("Spectator"), CENTER));
		
		y += ROW_HEIGHT * 7 / 8;
		
		generateLeftSideText(y, elements, text("Speed Multiplier (Default)"));
		elements.add(new DiscreteValueSliderWidget<>(col4(2), y, COL4_W, flightSpeedOptions, cfg.flightSpeedMpCreativeDefault, value -> cfg.flightSpeedMpCreativeDefault = value));
		elements.add(new DiscreteValueSliderWidget<>(col4(3), y, COL4_W, flightSpeedOptions, cfg.flightSpeedMpSpectatorDefault, value -> cfg.flightSpeedMpSpectatorDefault = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Speed Multiplier (Sprinting)"));
		elements.add(new DiscreteValueSliderWidget<>(col4(2), y, COL4_W, flightSpeedOptions, cfg.flightSpeedMpCreativeSprinting, value -> cfg.flightSpeedMpCreativeSprinting = value));
		elements.add(new DiscreteValueSliderWidget<>(col4(3), y, COL4_W, flightSpeedOptions, cfg.flightSpeedMpSpectatorSprinting, value -> cfg.flightSpeedMpSpectatorSprinting = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Vertical Speed Boost (Default)"));
		elements.add(new DiscreteValueSliderWidget<>(col4(2), y, COL4_W, flightVerticalBoostOptions, cfg.flightVerticalBoostCreativeDefault, value -> cfg.flightVerticalBoostCreativeDefault = value));
		elements.add(new DiscreteValueSliderWidget<>(col4(3), y, COL4_W, flightVerticalBoostOptions, cfg.flightVerticalBoostSpectatorDefault, value -> cfg.flightVerticalBoostSpectatorDefault = value));
		
		y += ROW_HEIGHT;
		
		generateLeftSideText(y, elements, text("Vertical Speed Boost (Sprinting)"));
		elements.add(new DiscreteValueSliderWidget<>(col4(2), y, COL4_W, flightVerticalBoostOptions, cfg.flightVerticalBoostCreativeSprinting, value -> cfg.flightVerticalBoostCreativeSprinting = value));
		elements.add(new DiscreteValueSliderWidget<>(col4(3), y, COL4_W, flightVerticalBoostOptions, cfg.flightVerticalBoostSpectatorSprinting, value -> cfg.flightVerticalBoostSpectatorSprinting = value));
		
		y += ROW_HEIGHT;
		return y;
	}
	
	private int generateMiscellaneousOptions(int y, final List<IGuiEventListener> elements){
		final BetterControlsConfig cfg = BetterControlsMod.config;
		
		generateKeyBindingWithModifierOption(y, elements, text("Toggle Walk Forwards"), cfg.keyToggleWalkForward);
		
		y += ROW_HEIGHT;
		
		generateKeyBindingWithModifierOption(y, elements, text("Toggle Jump"), cfg.keyToggleJump);
		
		y += ROW_HEIGHT;
		
		generateKeyBindingWithModifierOption(y, elements, text("Reset All Toggles"), cfg.keyResetAllToggles);
		
		y += ROW_HEIGHT * 4 / 3;
		
		generateKeyBindingWithModifierOption(y, elements, text("Open Better Controls Menu"), cfg.keyOpenMenu);
		
		y += ROW_HEIGHT;
		return y;
	}
	
	// Helpers
	
	private static final List<Option<ModifierKey>> MODIFIER_OPTIONS = Arrays.asList(
		new Option<>(null, text("(No Modifier)")),
		new Option<>(ModifierKey.CONTROL, text("Control")),
		new Option<>(ModifierKey.SHIFT, text("Shift")),
		new Option<>(ModifierKey.ALT, text("Alt"))
	);
	
	private void generateKeyBindingWithModifierOption(final int y, final List<IGuiEventListener> elements, final LiteralText text, final KeyBindingWithModifier binding){
		final CycleButtonWidget<ModifierKey> modifierButton = new CycleButtonWidget<>(col4(2), y, COL4_W, MODIFIER_OPTIONS, binding.getModifier(), binding::setModifier);
		final KeyBindingWidget bindingButton = new KeyBindingWidget(col4(3), y, COL4_W, binding, this::startEditingKeyBinding);
		bindingButton.linkButtonToBoundState(modifierButton);
		
		generateLeftSideText(y, elements, text);
		elements.add(modifierButton);
		elements.add(bindingButton);
		allKeyBindings.add(bindingButton);
	}
	
	private static void generateLeftSideText(final int y, final List<IGuiEventListener> elements, final LiteralText text){
		elements.add(new TextWidget(col2(0), y, COL2_W - TEXT_PADDING_RIGHT, text));
	}
	
	// Instance
	
	private OptionListWidget optionsWidget;
	private KeyBindingWidget editingKeyBinding;
	private final List<KeyBindingWidget> allKeyBindings = new ArrayList<>();
	
	public BetterControlsScreen(@Nullable final Screen parentScreen){
		super(parentScreen, OPTIONS, TITLE);
	}
	
	@Override
	public void init(){
		allKeyBindings.clear();
		
		final List<IGuiEventListener> elements = new ArrayList<>();
		int y = 0;
		
		elements.add(new TextWidget(0, y, ROW_WIDTH, ROW_HEIGHT, text("Sprinting"), CENTER));
		y = generateSprintingOptions(y + ROW_HEIGHT, elements) + TITLE_MARGIN_TOP;
		
		elements.add(new TextWidget(0, y, ROW_WIDTH, ROW_HEIGHT, text("Sneaking"), CENTER));
		y = generateSneakingOptions(y + ROW_HEIGHT, elements) + TITLE_MARGIN_TOP;
		
		elements.add(new TextWidget(0, y, ROW_WIDTH, ROW_HEIGHT, text("Flying"), CENTER));
		y = generateFlightOptions(y + ROW_HEIGHT, elements) + TITLE_MARGIN_TOP;
		
		elements.add(new TextWidget(0, y, ROW_WIDTH, ROW_HEIGHT, text("Miscellaneous"), CENTER));
		y = generateMiscellaneousOptions(y + ROW_HEIGHT, elements) + TITLE_MARGIN_TOP;
		
		addButton(new Button(width / 2 - 99, height - 29, 200, 20, I18n.format("gui.done"), btn -> minecraft.displayGuiScreen(parentScreen)));
		children.add(optionsWidget = new OptionListWidget(21, height - 32, width, height, elements, y - TITLE_MARGIN_TOP + BOTTOM_PADDING));
	}
	
	@Override
	public void removed(){
		BetterControlsMod.config.save();
	}
	
	@Override
	public void render(final int mouseX, final int mouseY, final float delta){
		renderBackground();
		optionsWidget.render(mouseX, mouseY, delta);
		drawCenteredString(font, title.getString(), width / 2, 8, (255 << 16) | (255 << 8) | 255);
		super.render(mouseX, mouseY, delta);
	}
	
	private void startEditingKeyBinding(final KeyBindingWidget widget){
		if (editingKeyBinding != null){
			editingKeyBinding.stopEditing();
		}
		
		editingKeyBinding = widget;
	}
	
	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button){
		if (editingKeyBinding != null){
			editingKeyBinding.bindAndStopEditing(Key.inputFromMouse(button));
			onKeyBindingEditingFinished();
			return true;
		}
		else{
			return super.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers){
		if (editingKeyBinding != null){
			if (keyCode == GLFW.GLFW_KEY_ESCAPE){
				editingKeyBinding.bindAndStopEditing(Key.INVALID);
			}
			else{
				editingKeyBinding.bindAndStopEditing(Key.inputFromKeyboard(keyCode, scanCode));
			}
			
			onKeyBindingEditingFinished();
			return true;
		}
		else{
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
	}
	
	private void onKeyBindingEditingFinished(){
		editingKeyBinding = null;
		KeyBinding.resetKeyBindingArrayAndHash();
		
		for(final KeyBindingWidget widget : allKeyBindings){
			widget.updateKeyBindingText();
		}
	}
}
