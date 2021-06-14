package io.github.prospector.modmenu.api;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

@SuppressWarnings("unused")
public interface ModMenuApi {
	default ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ignore -> null;
	}
	
	default Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
		return ImmutableMap.of();
	}
}
