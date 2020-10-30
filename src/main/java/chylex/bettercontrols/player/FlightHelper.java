package chylex.bettercontrols.player;
import chylex.bettercontrols.BetterControlsMod;
import chylex.bettercontrols.config.BetterControlsConfig;
import net.minecraft.client.entity.player.ClientPlayerEntity;

final class FlightHelper{
	private FlightHelper(){}
	
	private static final float BASE_FLIGHT_SPEED = 0.05F;
	private static final float BASE_FLIGHT_SPEED_SPRINT_MP_INV = 0.5F; // sprinting doubles speed in PlayerEntity.travel
	private static final float BASE_VERTICAL_VELOCITY = 3F;
	
	private static BetterControlsConfig cfg(){
		return BetterControlsMod.config;
	}
	
	static boolean isFlyingCreativeOrSpectator(final ClientPlayerEntity player){
		return player.abilities.isFlying && (player.isCreative() || player.isSpectator());
	}
	
	static boolean shouldFlyOnGround(final ClientPlayerEntity player){
		return cfg().flyOnGroundInCreative && player.isCreative() && player.abilities.isFlying;
	}
	
	static float getFlightSpeed(final ClientPlayerEntity player){
		if (player.isCreative()){
			if (player.isSprinting()){
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpCreativeSprinting * BASE_FLIGHT_SPEED_SPRINT_MP_INV;
			}
			else{
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpCreativeDefault;
			}
		}
		else if (player.isSpectator()){
			if (player.isSprinting()){
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpSpectatorSprinting * BASE_FLIGHT_SPEED_SPRINT_MP_INV;
			}
			else{
				return BASE_FLIGHT_SPEED * cfg().flightSpeedMpSpectatorDefault;
			}
		}
		else{
			return 0F;
		}
	}
	
	static float getExtraVerticalVelocity(final ClientPlayerEntity player){
		if (player.isCreative()){
			if (player.isSprinting()){
				return BASE_VERTICAL_VELOCITY * cfg().flightVerticalBoostCreativeSprinting;
			}
			else{
				return BASE_VERTICAL_VELOCITY * cfg().flightVerticalBoostCreativeDefault;
			}
		}
		else if (player.isSpectator()){
			if (player.isSprinting()){
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
