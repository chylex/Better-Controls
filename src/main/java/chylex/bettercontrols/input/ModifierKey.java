package chylex.bettercontrols.input;

import net.minecraft.client.Minecraft;

public enum ModifierKey {
	CONTROL(0) {
		@Override
		public boolean isPressed() {
			return Minecraft.getInstance().hasControlDown();
		}
	},
	
	SHIFT(1) {
		@Override
		public boolean isPressed() {
			return Minecraft.getInstance().hasShiftDown();
		}
	},
	
	ALT(2) {
		@Override
		public boolean isPressed() {
			return Minecraft.getInstance().hasAltDown();
		}
	};
	
	public final int id;
	
	ModifierKey(int id) {
		this.id = id;
	}
	
	public abstract boolean isPressed();
	
	public static ModifierKey getById(int id) {
		return switch (id) {
			case 0 -> CONTROL;
			case 1 -> SHIFT;
			case 2 -> ALT;
			default -> null;
		};
	}
}
