package io.github.prospector.modmenu.api;
import net.minecraft.client.gui.screens.Screen;

@SuppressWarnings("unused")
@FunctionalInterface
public interface ConfigScreenFactory<S extends Screen> {
	S create(Screen var1);
}
