package chylex.bettercontrols.input;

import net.minecraft.client.KeyMapping;

public class ToggleTracker {
	protected final KeyMapping bindingToggle;
	protected final KeyMapping bindingReset;
	
	private boolean isToggled;
	private boolean waitForRelease;
	private boolean hasToggledWhileHoldingReset;
	private boolean skipNextToggle;
	
	protected ToggleTracker(KeyMapping bindingToggle, KeyMapping bindingReset, boolean initialState) {
		this.bindingToggle = bindingToggle;
		this.bindingReset = bindingReset;
		this.isToggled = initialState;
	}
	
	public ToggleTracker(KeyMapping bindingToggle, KeyMapping bindingReset) {
		this(bindingToggle, bindingReset, false);
	}
	
	/*
	 * Assume holding CTRL is used to sprint (reset key) and either G or CTRL + G is used to toggle sprint on.
	 * The toggle modifier actually does not matter, having it just prevents trigger if the key is pressed alone.
	 *
	 * Pressing the toggle key alone switches the toggled state, then the key has to be released.
	 * Holding the reset key while toggled will reset the toggle, but continue the sprint until released.
	 *
	 * Pressing the toggle key while holding reset key switches the toggled state, but allows both keys to be
	 * released without interrupting the toggle (hasToggledWhileHoldingReset).
	 *
	 * Note that holding the reset key and pressing toggle key twice will switch the toggle twice, as expected.
	 *
	 * Holding the reset key while toggled, then pressing the toggle key again, will do nothing (skipNextToggle).
	 * This allows resetting the toggle both by pressing the reset key alone, or with the full toggle key combo.
	 * However, if the toggle key is then pressed again, it will function as usual.
	 *
	 * All of the logic combined allows for complex scenarios such as:
	 *
	 *   +CTRL, +G, -CTRL                 -->  toggled on
	 *   +CTRL, +G, -CTRL, +CTRL          -->  toggled off (quick reset)
	 *   +CTRL, +G, -CTRL, +CTRL, +G      -->  toggled off (full combo)
	 *   +CTRL, +G, -CTRL, +CTRL, +G, +G  -->  toggled on
	 */
	
	public boolean tick() {
		boolean isHoldingReset = isResetKeyPressed();
		
		if (bindingToggle.isDown()) {
			if (!waitForRelease) {
				if (skipNextToggle) {
					skipNextToggle = false;
				}
				else {
					isToggled = !isToggled;
				}
				
				waitForRelease = true;
				hasToggledWhileHoldingReset = isHoldingReset;
			}
		}
		else {
			waitForRelease = false;
		}
		
		if (isToggled) {
			if (hasToggledWhileHoldingReset && !isHoldingReset) {
				hasToggledWhileHoldingReset = false;
			}
			else if (!hasToggledWhileHoldingReset && isHoldingReset) {
				isToggled = false;
				skipNextToggle = true;
			}
		}
		
		if (skipNextToggle && !isHoldingReset) {
			skipNextToggle = false;
		}
		
		return isToggled;
	}
	
	protected boolean isResetKeyPressed() {
		return bindingReset.isDown();
	}
	
	public void reset() {
		isToggled = false;
		waitForRelease = false;
	}
}
