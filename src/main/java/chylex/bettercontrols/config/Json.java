package chylex.bettercontrols.config;
import chylex.bettercontrols.input.KeyBindingWithModifier;
import chylex.bettercontrols.input.ModifierKey;
import com.google.gson.JsonObject;
import net.minecraft.client.util.InputUtil;

final class Json{
	private Json(){}
	
	static void setInt(final JsonObject obj, final String key, final int value){
		obj.addProperty(key, Integer.valueOf(value));
	}
	
	static int getInt(final JsonObject obj, final String key, final int defaultValue){
		return obj.has(key) ? obj.get(key).getAsInt() : defaultValue;
	}
	
	static void setFloat(final JsonObject obj, final String key, final float value){
		obj.addProperty(key, Float.valueOf(value));
	}
	
	static float getFloat(final JsonObject obj, final String key, final float defaultValue){
		return obj.has(key) ? obj.get(key).getAsFloat() : defaultValue;
	}
	
	static void setBool(final JsonObject obj, final String key, final boolean value){
		obj.addProperty(key, Boolean.valueOf(value));
	}
	
	static boolean getBool(final JsonObject obj, final String key, final boolean defaultValue){
		return obj.has(key) ? obj.get(key).getAsBoolean() : defaultValue;
	}
	
	static <T extends Enum<T>> void setEnum(final JsonObject obj, final String key, final T value){
		obj.addProperty(key, value.name());
	}
	
	static <T extends Enum<T>> T getEnum(final JsonObject obj, final String key, final T defaultValue, final Class<T> enumClass){
		if (!obj.has(key)){
			return defaultValue;
		}
		
		final T[] constants = enumClass.getEnumConstants();
		
		if (constants != null){
			final String value = obj.get(key).getAsString();
			
			for(final T constant : constants){
				if (constant.name().equalsIgnoreCase(value)){
					return constant;
				}
			}
		}
		
		return defaultValue;
	}
	
	private static final String KEY_SUFFIX = ".Key";
	private static final String MOD_SUFFIX = ".Mod";
	
	static void writeKeyBinding(final JsonObject obj, final String key, final KeyBindingWithModifier keyBinding){
		obj.addProperty(key + KEY_SUFFIX, keyBinding.getBoundKeyTranslationKey());
		
		if (keyBinding.getModifier() != null){
			obj.addProperty(key + MOD_SUFFIX, Integer.valueOf(keyBinding.getModifier().id));
		}
	}
	
	static void readKeyBinding(final JsonObject obj, final String key, final KeyBindingWithModifier keyBinding){
		if (obj.has(key + KEY_SUFFIX)){
			keyBinding.setBoundKey(InputUtil.fromTranslationKey(obj.get(key + KEY_SUFFIX).getAsString()));
		}
		
		if (obj.has(key + MOD_SUFFIX)){
			keyBinding.setModifier(ModifierKey.getById(obj.get(key + MOD_SUFFIX).getAsInt()));
		}
	}
}
