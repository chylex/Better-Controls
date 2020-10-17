package chylex.bettercontrols.gui;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.gui.elements.BooleanValueWidget;
import chylex.bettercontrols.gui.elements.KeyBindingWidget;
import chylex.bettercontrols.gui.elements.TextWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import static chylex.bettercontrols.gui.OptionListWidget.COL2_W;
import static chylex.bettercontrols.gui.OptionListWidget.ROW_WIDTH;
import static chylex.bettercontrols.gui.OptionListWidget.col2;
import static chylex.bettercontrols.gui.elements.TextWidget.CENTER;

public class BetterControlsScreen extends GameOptionsScreen{
	public static final LiteralText TITLE = new LiteralText("Better Controls");
	
	private static final int BOTTOM_PADDING = 3;
	private static final int TEXT_PADDING_RIGHT = 4;
	private static final int TITLE_MARGIN_TOP = 3;
	private static final int ROW_HEIGHT = 22;
	
	// Options
	
	private int generateSprintingOptions(int y, final List<Element> elements){
		final BetterControlsConfig cfg = BetterControlsMod.config;
		
		generateLeftSideText(y, elements, Text.of("Double Tap 'Walk Forwards' To Sprint"));
		elements.add(new BooleanValueWidget(col2(1), y, COL2_W, cfg.doubleTapForwardToSprint, value -> cfg.doubleTapForwardToSprint = value));
		
		y += ROW_HEIGHT;
		return y;
	}
	
	// Helpers
	
	private static void generateLeftSideText(final int y, final List<Element> elements, final Text text){
		elements.add(new TextWidget(col2(0), y, COL2_W - TEXT_PADDING_RIGHT, text));
	}
	
	// Instance
	
	private OptionListWidget optionsWidget;
	private KeyBindingWidget editingKeyBinding;
	private final List<KeyBindingWidget> allKeyBindings = new ArrayList<>();
	
	public BetterControlsScreen(final Screen parentScreen){
		super(parentScreen, MinecraftClient.getInstance().options, TITLE);
	}
	
	@Override
	public void init(){
		allKeyBindings.clear();
		
		final List<Element> elements = new ArrayList<>();
		int y = 0;
		
		elements.add(new TextWidget(0, y, ROW_WIDTH, ROW_HEIGHT, Text.of("Sprinting"), CENTER));
		y = generateSprintingOptions(y + ROW_HEIGHT, elements) + TITLE_MARGIN_TOP;
		
		addButton(new ButtonWidget(width / 2 - 99, height - 29, 200, 20, ScreenTexts.DONE, btn -> client.openScreen(parent)));
		addChild(optionsWidget = new OptionListWidget(21, height - 32, width, height, elements, y - TITLE_MARGIN_TOP + BOTTOM_PADDING));
	}
	
	@Override
	public void removed(){
		BetterControlsMod.config.save();
	}
	
	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta){
		renderBackground(matrices);
		optionsWidget.render(matrices, mouseX, mouseY, delta);
		drawCenteredText(matrices, textRenderer, title, width / 2, 8, (255 << 16) | (255 << 8) | 255);
		super.render(matrices, mouseX, mouseY, delta);
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
			editingKeyBinding.bindAndStopEditing(InputUtil.Type.MOUSE.createFromCode(button));
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
				editingKeyBinding.bindAndStopEditing(InputUtil.UNKNOWN_KEY);
			}
			else{
				editingKeyBinding.bindAndStopEditing(InputUtil.fromKeyCode(keyCode, scanCode));
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
		KeyBinding.updateKeysByCode();
		
		for(final KeyBindingWidget widget : allKeyBindings){
			widget.updateKeyBindingText();
		}
	}
}
