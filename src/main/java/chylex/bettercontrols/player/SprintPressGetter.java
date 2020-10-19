package chylex.bettercontrols.player;
import java.util.function.BooleanSupplier;

final class SprintPressGetter implements BooleanSupplier{
	private final BooleanSupplier wrapped;
	private final BooleanSupplier or;
	
	public SprintPressGetter(final BooleanSupplier wrapped, final BooleanSupplier or){
		this.wrapped = wrapped;
		this.or = or;
	}
	
	public BooleanSupplier getWrapped(){
		return wrapped;
	}
	
	@Override
	public boolean getAsBoolean(){
		return wrapped.getAsBoolean() || or.getAsBoolean();
	}
}
