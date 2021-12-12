package com.terraformersmc.modmenu.api;

@SuppressWarnings("unused")
public interface ModMenuApi {
	default ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> null;
	}
}
