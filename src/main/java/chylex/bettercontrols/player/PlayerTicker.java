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
	
	private boolean wasSneakingBeforeTouchingGround = false;
	private boolean holdingSneakWhileTouchingGround = false;
	
	public void atHead(final ClientPlayerEntity player){
		if (FlightHelper.shouldFlyOnGround(player)){
			player.setOnGround(false);
		}
		
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
	
	public void afterSuperCall(final ClientPlayerEntity player){
		if (FlightHelper.shouldFlyOnGround(player)){
			final boolean isSneaking = player.isSneaking();
			final boolean isOnGround = player.isOnGround();
			
			if (!isSneaking){
				wasSneakingBeforeTouchingGround = false;
			}
			else if (!isOnGround){
				wasSneakingBeforeTouchingGround = true;
			}
			
			if (!isOnGround){
				holdingSneakWhileTouchingGround = false;
			}
			else{
				boolean cancelLanding = true;
				
				if (!wasSneakingBeforeTouchingGround){
					if (isSneaking){
						holdingSneakWhileTouchingGround = true;
					}
					else if (holdingSneakWhileTouchingGround){
						player.abilities.flying = false;
						player.sendAbilitiesUpdate();
						cancelLanding = false;
					}
				}
				
				if (cancelLanding){
					player.setOnGround(false);
				}
			}
		}
		else{
			wasSneakingBeforeTouchingGround = false;
			holdingSneakWhileTouchingGround = false;
		}
	}
}
