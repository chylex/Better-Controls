package chylex.bettercontrols.config;
import chylex.bettercontrols.input.SprintMode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

final class ConfigSerializer implements JsonSerializer<BetterControlsConfig>, JsonDeserializer<BetterControlsConfig>{
	private static final Logger logger = LogManager.getLogger();
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(BetterControlsConfig.class, new ConfigSerializer()).setPrettyPrinting().create();
	
	private ConfigSerializer(){}
	
	@Override
	public JsonElement serialize(final BetterControlsConfig cfg, final Type typeOfSrc, final JsonSerializationContext context){
		final JsonObject obj = new JsonObject();
		
		Json.writeKeyBinding(obj, "Sprint.KeyToggle", cfg.keyToggleSprint);
		Json.setEnum(obj, "Sprint.Mode", cfg.sprintMode);
		Json.setBool(obj, "Sprint.DoubleTapForward", cfg.doubleTapForwardToSprint);
		Json.setBool(obj, "Sprint.ResumeAfterHittingObstacle", cfg.resumeSprintingAfterHittingObstacle);
		
		Json.writeKeyBinding(obj, "Sneak.KeyToggle", cfg.keyToggleSneak);
		Json.setBool(obj, "Sneak.SmoothCamera", cfg.sneakingMovesCameraSmoothly);
		
		Json.writeKeyBinding(obj, "Flight.KeyToggle.Creative", cfg.keyToggleFlight);
		Json.setEnum(obj, "Flight.SprintMode", cfg.sprintModeWhileFlying);
		Json.setBool(obj, "Flight.DisableInertia", cfg.disableFlightInertia);
		Json.setBool(obj, "Flight.DisableChangingFOV", cfg.disableChangingFovWhileFlying);
		Json.setBool(obj, "Flight.FlyOnGround.Creative", cfg.flyOnGroundInCreative);
		Json.setFloat(obj, "Flight.SpeedMp.Creative.Default", cfg.flightSpeedMpCreativeDefault);
		Json.setFloat(obj, "Flight.SpeedMp.Creative.Sprinting", cfg.flightSpeedMpCreativeSprinting);
		Json.setFloat(obj, "Flight.SpeedMp.Spectator.Default", cfg.flightSpeedMpSpectatorDefault);
		Json.setFloat(obj, "Flight.SpeedMp.Spectator.Sprinting", cfg.flightSpeedMpSpectatorSprinting);
		Json.setFloat(obj, "Flight.VerticalBoost.Creative.Default", cfg.flightVerticalBoostCreativeDefault);
		Json.setFloat(obj, "Flight.VerticalBoost.Creative.Sprinting", cfg.flightVerticalBoostCreativeSprinting);
		Json.setFloat(obj, "Flight.VerticalBoost.Spectator.Default", cfg.flightVerticalBoostSpectatorDefault);
		Json.setFloat(obj, "Flight.VerticalBoost.Spectator.Sprinting", cfg.flightVerticalBoostSpectatorSprinting);
		
		Json.writeKeyBinding(obj, "Misc.KeyToggleWalkForward", cfg.keyToggleWalkForward);
		Json.writeKeyBinding(obj, "Misc.KeyToggleJump", cfg.keyToggleJump);
		Json.writeKeyBinding(obj, "Misc.KeyResetAllToggles", cfg.keyResetAllToggles);
		Json.writeKeyBinding(obj, "Misc.KeyOpenMenu", cfg.keyOpenMenu);
		
		return obj;
	}
	
	@Override
	public BetterControlsConfig deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException{
		final BetterControlsConfig cfg = new BetterControlsConfig();
		final JsonObject obj = json.getAsJsonObject();
		
		if (obj.has("Sprint.TapToStop") && obj.get("Sprint.TapToStop").getAsBoolean()){
			cfg.sprintMode = SprintMode.TAP_TO_TOGGLE;
		}
		
		Json.readKeyBinding(obj, "Sprint.KeyToggle", cfg.keyToggleSprint);
		cfg.sprintMode = Json.getEnum(obj, "Sprint.Mode", cfg.sprintMode, SprintMode.class);
		cfg.doubleTapForwardToSprint = Json.getBool(obj, "Sprint.DoubleTapForward", cfg.doubleTapForwardToSprint);
		cfg.resumeSprintingAfterHittingObstacle = Json.getBool(obj, "Sprint.ResumeAfterHittingObstacle", cfg.resumeSprintingAfterHittingObstacle);
		
		Json.readKeyBinding(obj, "Sneak.KeyToggle", cfg.keyToggleSneak);
		cfg.sneakingMovesCameraSmoothly = Json.getBool(obj, "Sneak.SmoothCamera", cfg.sneakingMovesCameraSmoothly);
		
		Json.readKeyBinding(obj, "Flight.KeyToggle.Creative", cfg.keyToggleFlight);
		cfg.sprintModeWhileFlying = Json.getEnum(obj, "Flight.SprintMode", cfg.sprintModeWhileFlying, SprintMode.class);
		cfg.disableFlightInertia = Json.getBool(obj, "Flight.DisableInertia", cfg.disableFlightInertia);
		cfg.disableChangingFovWhileFlying = Json.getBool(obj, "Flight.DisableChangingFOV", cfg.disableChangingFovWhileFlying);
		cfg.flyOnGroundInCreative = Json.getBool(obj, "Flight.FlyOnGround.Creative", cfg.flyOnGroundInCreative);
		cfg.flightSpeedMpCreativeDefault = Json.getFloat(obj, "Flight.SpeedMp.Creative.Default", cfg.flightSpeedMpCreativeDefault, 0.25F, 8F);
		cfg.flightSpeedMpCreativeSprinting = Json.getFloat(obj, "Flight.SpeedMp.Creative.Sprinting", cfg.flightSpeedMpCreativeSprinting, 0.25F, 8F);
		cfg.flightSpeedMpSpectatorDefault = Json.getFloat(obj, "Flight.SpeedMp.Spectator.Default", cfg.flightSpeedMpSpectatorDefault, 0.25F, 8F);
		cfg.flightSpeedMpSpectatorSprinting = Json.getFloat(obj, "Flight.SpeedMp.Spectator.Sprinting", cfg.flightSpeedMpSpectatorSprinting, 0.25F, 8F);
		cfg.flightVerticalBoostCreativeDefault = Json.getFloat(obj, "Flight.VerticalBoost.Creative.Default", cfg.flightVerticalBoostCreativeDefault, 0F, 3F);
		cfg.flightVerticalBoostCreativeSprinting = Json.getFloat(obj, "Flight.VerticalBoost.Creative.Sprinting", cfg.flightVerticalBoostCreativeSprinting, 0F, 3F);
		cfg.flightVerticalBoostSpectatorDefault = Json.getFloat(obj, "Flight.VerticalBoost.Spectator.Default", cfg.flightVerticalBoostSpectatorDefault, 0F, 3F);
		cfg.flightVerticalBoostSpectatorSprinting = Json.getFloat(obj, "Flight.VerticalBoost.Spectator.Sprinting", cfg.flightVerticalBoostSpectatorSprinting, 0F, 3F);
		
		Json.readKeyBinding(obj, "Misc.KeyToggleWalkForward", cfg.keyToggleWalkForward);
		Json.readKeyBinding(obj, "Misc.KeyToggleJump", cfg.keyToggleJump);
		Json.readKeyBinding(obj, "Misc.KeyResetAllToggles", cfg.keyResetAllToggles);
		Json.readKeyBinding(obj, "Misc.KeyOpenMenu", cfg.keyOpenMenu);
		
		return cfg;
	}
	
	static void serialize(final Path path, final BetterControlsConfig config){
		try(final JsonWriter writer = gson.newJsonWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))){
			gson.getAdapter(BetterControlsConfig.class).write(writer, config);
		}catch(final IOException e){
			logger.error("Error saving BetterControls configuration file!", e);
		}
	}
	
	static BetterControlsConfig deserialize(final Path path){
		try(final JsonReader jsonReader = new JsonReader(Files.newBufferedReader(path, StandardCharsets.UTF_8))){
			return gson.getAdapter(BetterControlsConfig.class).read(jsonReader);
		}catch(final FileNotFoundException | NoSuchFileException ignored){
		}catch(final IOException e){
			logger.error("Error reading BetterControls configuration file!", e);
		}
		
		return new BetterControlsConfig();
	}
}
