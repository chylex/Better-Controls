package chylex.bettercontrols.config;

import chylex.bettercontrols.input.KeyBindingWithModifier;
import chylex.bettercontrols.input.ModifierKey;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;

final class Json {
	private Json() {}
	
	static void setFloat(JsonObject obj, String key, float value) {
		obj.addProperty(key, Float.valueOf(value));
	}
	
	static float getFloat(JsonObject obj, String key, float defaultValue, float minValue, float maxValue) {
		float value = obj.has(key) ? obj.get(key).getAsFloat() : defaultValue;
		return Math.max(minValue, Math.min(maxValue, value));
	}
	
	static void setBool(JsonObject obj, String key, boolean value) {
		obj.addProperty(key, Boolean.valueOf(value));
	}
	
	static boolean getBool(JsonObject obj, String key, boolean defaultValue) {
		return obj.has(key) ? obj.get(key).getAsBoolean() : defaultValue;
	}
	
	@SuppressWarnings("SameParameterValue")
	static <T extends Enum<T>> void setEnum(JsonObject obj, String key, T value) {
		obj.addProperty(key, value.name());
	}
	
	@SuppressWarnings("SameParameterValue")
	static <T extends Enum<T>> T getEnum(JsonObject obj, String key, T defaultValue, Class<T> enumClass) {
		if (!obj.has(key)) {
			return defaultValue;
		}
		
		T[] constants = enumClass.getEnumConstants();
		
		if (constants != null) {
			String value = obj.get(key).getAsString();
			
			for (T constant : constants) {
				if (constant.name().equalsIgnoreCase(value)) {
					return constant;
				}
			}
		}
		
		return defaultValue;
	}
	
	private static final String KEY_SUFFIX = ".Key";
	private static final String MOD_SUFFIX = ".Mod";
	
	static void writeKeyBinding(JsonObject obj, String key, KeyBindingWithModifier keyBinding) {
		obj.addProperty(key + KEY_SUFFIX, keyBinding.saveString());
		
		if (keyBinding.getModifier() != null) {
			obj.addProperty(key + MOD_SUFFIX, Integer.valueOf(keyBinding.getModifier().id));
		}
	}
	
	static void readKeyBinding(JsonObject obj, String key, KeyBindingWithModifier keyBinding) {
		if (obj.has(key + KEY_SUFFIX)) {
			try {
				keyBinding.setKey(InputConstants.getKey(obj.get(key + KEY_SUFFIX).getAsString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace(); // let's not crash if the config file has garbage, okay?
			}
		}
		
		if (obj.has(key + MOD_SUFFIX)) {
			keyBinding.setModifier(ModifierKey.getById(obj.get(key + MOD_SUFFIX).getAsInt()));
		}
	}
}
