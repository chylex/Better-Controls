package chylex.bettercontrols.config;
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
		
		Json.setBool(obj, "Sprint.DoubleTapForward", cfg.doubleTapForwardToSprint);
		
		Json.setBool(obj, "Flight.FlyOnGround.Creative", cfg.flyOnGroundInCreative);
		Json.setFloat(obj, "Flight.SpeedMp.Creative.Default", cfg.flightSpeedMpCreativeDefault);
		Json.setFloat(obj, "Flight.SpeedMp.Creative.Sprinting", cfg.flightSpeedMpCreativeSprinting);
		Json.setFloat(obj, "Flight.SpeedMp.Spectator.Default", cfg.flightSpeedMpSpectatorDefault);
		Json.setFloat(obj, "Flight.SpeedMp.Spectator.Sprinting", cfg.flightSpeedMpSpectatorSprinting);
		
		return obj;
	}
	
	@Override
	public BetterControlsConfig deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException{
		final BetterControlsConfig cfg = new BetterControlsConfig();
		final JsonObject obj = json.getAsJsonObject();
		
		cfg.doubleTapForwardToSprint = Json.getBool(obj, "Sprint.DoubleTapForward", cfg.doubleTapForwardToSprint);
		
		cfg.flyOnGroundInCreative = Json.getBool(obj, "Flight.FlyOnGround.Creative", cfg.flyOnGroundInCreative);
		cfg.flightSpeedMpCreativeDefault = Json.getFloat(obj, "Flight.SpeedMp.Creative.Default", cfg.flightSpeedMpCreativeDefault);
		cfg.flightSpeedMpCreativeSprinting = Json.getFloat(obj, "Flight.SpeedMp.Creative.Sprinting", cfg.flightSpeedMpCreativeSprinting);
		cfg.flightSpeedMpSpectatorDefault = Json.getFloat(obj, "Flight.SpeedMp.Spectator.Default", cfg.flightSpeedMpSpectatorDefault);
		cfg.flightSpeedMpSpectatorSprinting = Json.getFloat(obj, "Flight.SpeedMp.Spectator.Sprinting", cfg.flightSpeedMpSpectatorSprinting);
		
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
