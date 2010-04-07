package flock;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Convenience class defining the key "operations" we care about.
 * The actual key codes used are in Config.
 */
public abstract class FlockKeyListener implements KeyListener
{
	enum Key
	{
		None, ///< ignore
		Left,
		Right,
		Jump,
		TimeFreeze,
		PickUpItem,
		UseItem,
		Pause,
		Debug
	};
	
	public static Key getKey(KeyEvent e)
	{
		final int key = e.getKeyCode();
		return Game.instance().config().keyForCode(key);
	}
}
