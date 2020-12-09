package chylex.bettercontrols.player;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.config.BetterControlsConfig;
import net.minecraft.client.network.ClientPlayerEntity;

final class FlightHelper{
	private FlightHelper(){}
	
	private static final float BASE_FLIGHT_SPEED = 0.05F;
	private static final float BASE_VERTICAL_VELOCITY = 3F;
	
	private static BetterControlsConfig cfg(){
		return BetterControlsMod.config;
	}
	
	static boolean isFlyingCreativeOrSpectator(final ClientPlayerEntity player){
		return player.abilities.flying && (player.isCreative() || player.isSpectator());
	}
	
	static boolean shouldFlyOnGround(final ClientPlayerEntity player){
		return cfg().flyOnGroundInCreative && player.isCreative() && player.abilities.flying;
	}
	
	static float getFlightSpeed(final ClientPlayerEntity player, final boolean boost){
		if (player.isCreative()){
			if (boost){
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpCreativeSprinting;
			}
			else{
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpCreativeDefault;
			}
		}
		else if (player.isSpectator()){
			if (boost){
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpSpectatorSprinting;
			}
			else{
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpSpectatorDefault;
			}
		}
		else{
			return 0F;
		}
	}
	
	static float getExtraVerticalVelocity(final ClientPlayerEntity player, final boolean isSprinting){
		if (player.isCreative()){
			if (isSprinting){
				return BASE_VERTICAL_VELOCITY * cfg().flightVerticalBoostCreativeSprinting;
			}
			else{
				return BASE_VERTICAL_VELOCITY * cfg().flightVerticalBoostCreativeDefault;
			}
		}
		else if (player.isSpectator()){
			if (isSprinting){
				return BASE_VERTICAL_VELOCITY * cfg().flightVerticalBoostSpectatorSprinting;
			}
			else{
				return BASE_VERTICAL_VELOCITY * cfg().flightVerticalBoostSpectatorDefault;
			}
		}
		else{
			return 0F;
		}
	}
}
