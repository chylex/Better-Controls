package chylex.bettercontrols.player;

import java.util.function.BooleanSupplier;

record SprintPressGetter(BooleanSupplier wrapped, BooleanSupplier or) implements BooleanSupplier {
	@Override
	public boolean getAsBoolean() {
		return wrapped.getAsBoolean() || or.getAsBoolean();
	}
}
