package chylex.bettercontrols.player;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.config.BetterControlsConfig;
import chylex.bettercontrols.mixin.AccessClientPlayerFields;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import java.lang.ref.WeakReference;

public final class PlayerTicker{
	private static PlayerTicker ticker = new PlayerTicker(null);
	
	public static PlayerTicker get(final ClientPlayerEntity player){
		if (ticker.ref.get() != player){
			ticker = new PlayerTicker(player);
		}
		
		return ticker;
	}
	
	private static MinecraftClient mc(){
		return MinecraftClient.getInstance();
	}
	
	private static BetterControlsConfig cfg(){
		return BetterControlsMod.config;
	}
	
	private final WeakReference<ClientPlayerEntity> ref;
	
	private PlayerTicker(final ClientPlayerEntity player){
		this.ref = new WeakReference<>(player);
	}
	
	// Logic
	
	public void atHead(final ClientPlayerEntity player){
		if (!cfg().doubleTapForwardToSprint){
			((AccessClientPlayerFields)player).setTicksLeftToDoubleTapSprint(0);
		}
	}
	
	public void afterInputTick(final ClientPlayerEntity player){
		final float flightSpeed = FlightHelper.getFlightSpeed(player);
		
		if (flightSpeed > 0F){
			player.abilities.setFlySpeed(flightSpeed);
		}
	}
}
