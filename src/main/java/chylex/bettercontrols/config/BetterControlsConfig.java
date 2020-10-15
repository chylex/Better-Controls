package chylex.bettercontrols.config;
import net.minecraft.client.options.KeyBinding;
import java.nio.file.Path;

public final class BetterControlsConfig{
	public static BetterControlsConfig load(final Path path){
		return ConfigSerializer.deserialize(path).setPath(path);
	}
	
	private Path path;
	
	BetterControlsConfig(){}
	
	private BetterControlsConfig setPath(final Path path){
		this.path = path;
		return this;
	}
	
	public KeyBinding[] getAllKeyBindings(){
		return new KeyBinding[0];
	}
	
	public void save(){
		ConfigSerializer.serialize(path, this);
	}
}
