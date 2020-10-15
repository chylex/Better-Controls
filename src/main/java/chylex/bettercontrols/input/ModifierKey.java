package chylex.bettercontrols.input;
import net.minecraft.client.gui.screen.Screen;

public enum ModifierKey{
	CONTROL(0){
		@Override
		public boolean isPressed(){
			return Screen.hasControlDown();
		}
	},
	
	SHIFT(1){
		@Override
		public boolean isPressed(){
			return Screen.hasShiftDown();
		}
	},
	
	ALT(2){
		@Override
		public boolean isPressed(){
			return Screen.hasAltDown();
		}
	};
	
	public final int id;
	
	ModifierKey(final int id){
		this.id = id;
	}
	
	public abstract boolean isPressed();
	
	public static ModifierKey getById(final int id){
		switch(id){
			case 0: return CONTROL;
			case 1: return SHIFT;
			case 2: return ALT;
			default: return null;
		}
	}
}
