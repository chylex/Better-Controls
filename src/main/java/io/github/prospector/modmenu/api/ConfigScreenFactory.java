package io.github.prospector.modmenu.api;
import net.minecraft.client.gui.screen.Screen;

@SuppressWarnings("unused")
@FunctionalInterface
public interface ConfigScreenFactory<S extends Screen> {
	S create(Screen var1);
}
