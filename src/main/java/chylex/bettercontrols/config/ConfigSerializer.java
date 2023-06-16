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
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

final class ConfigSerializer implements JsonSerializer<BetterControlsConfig>, JsonDeserializer<BetterControlsConfig> {
	private static final Logger logger = LogManager.getLogger();
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(BetterControlsConfig.class, new ConfigSerializer()).setPrettyPrinting().create();
	
	private ConfigSerializer() {}
	
	@Override
	public JsonElement serialize(final BetterControlsConfig cfg, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonObject obj = new JsonObject();
		
		Json.writeKeyBinding(obj, "Sprint.KeyToggle", cfg.keyToggleSprint);
		Json.setEnum(obj, "Sprint.Mode", cfg.sprintMode);
		Json.setBool(obj, "Sprint.DoubleTapForward", cfg.doubleTapForwardToSprint);
		Json.setBool(obj, "Sprint.ResumeAfterHittingObstacle", cfg.resumeSprintingAfterHittingObstacle);
		
		Json.writeKeyBinding(obj, "Sneak.KeyToggle", cfg.keyToggleSneak);
		Json.setBool(obj, "Sneak.SmoothCamera", cfg.sneakingMovesCameraSmoothly);
		
		Json.writeKeyBinding(obj, "Flight.KeyToggle.Creative", cfg.keyToggleFlight);
		Json.setBool(obj, "Flight.DoubleTapJump", cfg.doubleTapJumpToToggleFlight);
		Json.setBool(obj, "Flight.DisableInertia", cfg.disableFlightInertia);
		Json.setBool(obj, "Flight.DisableChangingFOV", cfg.disableChangingFovWhileFlying);
		Json.setBool(obj, "Flight.FlyOnGround.Creative", cfg.flyOnGroundInCreative);
		Json.setFloat(obj, "Flight.SpeedMp.Creative.Default", cfg.flightHorizontalSpeedMpCreativeDefault);
		Json.setFloat(obj, "Flight.SpeedMp.Creative.Sprinting", cfg.flightHorizontalSpeedMpCreativeSprinting);
		Json.setFloat(obj, "Flight.SpeedMp.Spectator.Default", cfg.flightHorizontalSpeedMpSpectatorDefault);
		Json.setFloat(obj, "Flight.SpeedMp.Spectator.Sprinting", cfg.flightHorizontalSpeedMpSpectatorSprinting);
		Json.setFloat(obj, "Flight.VerticalSpeedMp.Creative.Default", cfg.flightVerticalSpeedMpCreativeDefault);
		Json.setFloat(obj, "Flight.VerticalSpeedMp.Creative.Sprinting", cfg.flightVerticalSpeedMpCreativeSprinting);
		Json.setFloat(obj, "Flight.VerticalSpeedMp.Spectator.Default", cfg.flightVerticalSpeedMpSpectatorDefault);
		Json.setFloat(obj, "Flight.VerticalSpeedMp.Spectator.Sprinting", cfg.flightVerticalSpeedMpSpectatorSprinting);
		
		Json.writeKeyBinding(obj, "Misc.KeyToggleWalkForward", cfg.keyToggleWalkForward);
		Json.writeKeyBinding(obj, "Misc.KeyToggleJump", cfg.keyToggleJump);
		Json.writeKeyBinding(obj, "Misc.KeyResetAllToggles", cfg.keyResetAllToggles);
		Json.writeKeyBinding(obj, "Misc.KeyOpenMenu", cfg.keyOpenMenu);
		
		return obj;
	}
	
	@Override
	public BetterControlsConfig deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		final BetterControlsConfig cfg = new BetterControlsConfig();
		final JsonObject obj = json.getAsJsonObject();
		
		if (obj.has("Sprint.TapToStop") && obj.get("Sprint.TapToStop").getAsBoolean()) {
			cfg.sprintMode = SprintMode.TAP_TO_TOGGLE;
		}
		
		Json.readKeyBinding(obj, "Sprint.KeyToggle", cfg.keyToggleSprint);
		cfg.sprintMode = Json.getEnum(obj, "Sprint.Mode", cfg.sprintMode, SprintMode.class);
		cfg.doubleTapForwardToSprint = Json.getBool(obj, "Sprint.DoubleTapForward", cfg.doubleTapForwardToSprint);
		cfg.resumeSprintingAfterHittingObstacle = Json.getBool(obj, "Sprint.ResumeAfterHittingObstacle", cfg.resumeSprintingAfterHittingObstacle);
		
		Json.readKeyBinding(obj, "Sneak.KeyToggle", cfg.keyToggleSneak);
		cfg.sneakingMovesCameraSmoothly = Json.getBool(obj, "Sneak.SmoothCamera", cfg.sneakingMovesCameraSmoothly);
		
		Json.readKeyBinding(obj, "Flight.KeyToggle.Creative", cfg.keyToggleFlight);
		cfg.doubleTapJumpToToggleFlight = Json.getBool(obj, "Flight.DoubleTapJump", cfg.doubleTapJumpToToggleFlight);
		cfg.disableFlightInertia = Json.getBool(obj, "Flight.DisableInertia", cfg.disableFlightInertia);
		cfg.disableChangingFovWhileFlying = Json.getBool(obj, "Flight.DisableChangingFOV", cfg.disableChangingFovWhileFlying);
		cfg.flyOnGroundInCreative = Json.getBool(obj, "Flight.FlyOnGround.Creative", cfg.flyOnGroundInCreative);
		cfg.flightHorizontalSpeedMpCreativeDefault = readHorizontalSpeedMultiplier(obj, "Flight.SpeedMp.Creative.Default", cfg.flightHorizontalSpeedMpCreativeDefault);
		cfg.flightHorizontalSpeedMpCreativeSprinting = readHorizontalSpeedMultiplier(obj, "Flight.SpeedMp.Creative.Sprinting", cfg.flightHorizontalSpeedMpCreativeSprinting);
		cfg.flightHorizontalSpeedMpSpectatorDefault = readHorizontalSpeedMultiplier(obj, "Flight.SpeedMp.Spectator.Default", cfg.flightHorizontalSpeedMpSpectatorDefault);
		cfg.flightHorizontalSpeedMpSpectatorSprinting = readHorizontalSpeedMultiplier(obj, "Flight.SpeedMp.Spectator.Sprinting", cfg.flightHorizontalSpeedMpSpectatorSprinting);
		cfg.flightVerticalSpeedMpCreativeDefault = readVerticalSpeedMultiplier(obj, cfg, "Flight.VerticalSpeedMp.Creative.Default", "Flight.VerticalBoost.Creative.Default", cfg.flightVerticalSpeedMpCreativeDefault);
		cfg.flightVerticalSpeedMpCreativeSprinting = readVerticalSpeedMultiplier(obj, cfg, "Flight.VerticalSpeedMp.Creative.Sprinting", "Flight.VerticalBoost.Creative.Sprinting", cfg.flightVerticalSpeedMpCreativeSprinting);
		cfg.flightVerticalSpeedMpSpectatorDefault = readVerticalSpeedMultiplier(obj, cfg, "Flight.VerticalSpeedMp.Spectator.Default", "Flight.VerticalBoost.Spectator.Default", cfg.flightVerticalSpeedMpSpectatorDefault);
		cfg.flightVerticalSpeedMpSpectatorSprinting = readVerticalSpeedMultiplier(obj, cfg, "Flight.VerticalSpeedMp.Spectator.Sprinting", "Flight.VerticalBoost.Spectator.Sprinting", cfg.flightVerticalSpeedMpSpectatorSprinting);
		
		Json.readKeyBinding(obj, "Misc.KeyToggleWalkForward", cfg.keyToggleWalkForward);
		Json.readKeyBinding(obj, "Misc.KeyToggleJump", cfg.keyToggleJump);
		Json.readKeyBinding(obj, "Misc.KeyResetAllToggles", cfg.keyResetAllToggles);
		Json.readKeyBinding(obj, "Misc.KeyOpenMenu", cfg.keyOpenMenu);
		
		return cfg;
	}
	
	private static float readHorizontalSpeedMultiplier(final JsonObject obj, final String key, final float defaultValue) {
		return Json.getFloat(obj, key, defaultValue, 0.25F, 8F);
	}
	
	private static float readVerticalSpeedMultiplier(final JsonObject obj, final BetterControlsConfig cfg, final String newKey, final String legacyBoostKey, final float defaultValue) {
		if (obj.has(newKey)) {
			return Json.getFloat(obj, newKey, defaultValue, 0.25F, 8F);
		}
		else if (obj.has(legacyBoostKey)) {
			cfg.wasMigrated = true;
			
			final float value = 1F + Json.getFloat(obj, legacyBoostKey, 0F, 0F, 3F);
			// 1.25x, 1.75x, 2.5x, and 3.5x are not supported
			if (Mth.equal(value, 1.25F) || Mth.equal(value, 1.75F)) {
				return 1.5F;
			}
			else if (Mth.equal(value, 2.5F)) {
				return 2F;
			}
			else if (Mth.equal(value, 3.5F)) {
				return 3F;
			}
			else {
				return value;
			}
		}
		else {
			return defaultValue;
		}
	}
	
	static void write(final Path path, final BetterControlsConfig config) {
		try (final JsonWriter writer = gson.newJsonWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
			gson.getAdapter(BetterControlsConfig.class).write(writer, config);
		} catch (final IOException e) {
			logger.error("Error saving BetterControls configuration file!", e);
		}
	}
	
	static BetterControlsConfig read(final Path path) {
		try (final JsonReader jsonReader = new JsonReader(Files.newBufferedReader(path, StandardCharsets.UTF_8))) {
			return gson.getAdapter(BetterControlsConfig.class).read(jsonReader);
		} catch (final FileNotFoundException | NoSuchFileException ignored) {
		} catch (final IOException e) {
			logger.error("Error reading BetterControls configuration file!", e);
		}
		
		return new BetterControlsConfig();
	}
}
