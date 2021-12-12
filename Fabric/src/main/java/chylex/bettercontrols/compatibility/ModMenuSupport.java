package chylex.bettercontrols.compatibility;
import chylex.bettercontrols.gui.BetterControlsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuSupport implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return BetterControlsScreen::new;
	}
}
