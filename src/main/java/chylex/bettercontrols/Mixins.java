package chylex.bettercontrols;

import chylex.bettercontrols.mixin.AccessCameraFields;
import chylex.bettercontrols.mixin.AccessClientPlayerFields;
import chylex.bettercontrols.mixin.AccessKeyMappingFields;
import chylex.bettercontrols.mixin.AccessPlayerFields;
import chylex.bettercontrols.mixin.AccessToggleKeyMappingFields;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("CastToIncompatibleInterface")
public final class Mixins {
	private Mixins() {}
	
	@SuppressWarnings("unchecked")
	public static <T> T me(Object object) {
		return (T) object;
	}
	
	public static AccessCameraFields cameraFields(Camera camera) {
		return (AccessCameraFields) camera;
	}
	
	public static AccessClientPlayerFields clientPlayerFields(LocalPlayer localPlayer) {
		return (AccessClientPlayerFields) localPlayer;
	}
	
	public static AccessKeyMappingFields keyMappingFields(KeyMapping keyMapping) {
		return (AccessKeyMappingFields) keyMapping;
	}
	
	public static AccessPlayerFields playerFields(Player player) {
		return (AccessPlayerFields) player;
	}
	
	public static AccessToggleKeyMappingFields toggleKeyMappingFields(ToggleKeyMapping toggleKeyMapping) {
		return (AccessToggleKeyMappingFields) toggleKeyMapping;
	}
}
