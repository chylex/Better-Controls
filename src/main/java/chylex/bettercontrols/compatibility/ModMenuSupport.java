package chylex.bettercontrols.compatibility;
import chylex.bettercontrols.gui.BetterControlsScreen;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuSupport implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return BetterControlsScreen::new;
	}
}
