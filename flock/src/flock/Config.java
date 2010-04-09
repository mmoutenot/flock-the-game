package flock;

import java.awt.event.KeyEvent;

/**
 * Holds all configuration data for the game.
 * The only instance is created in Game.
 * 
 * Right now everything is hardcoded, but having this as a
 * separate class allows us to use config files in the future.
 */
public class Config
{
	private int _paintFps;
	private int _updateFps;
	private int _tileWidth;
	private int _tileHeight;
	private double _defaultGravity;
	private double _defaultLemmingVelocity;
	private double _defaultPlayerMotionSpeed;
	private double _defaultPlayerJumpSpeed;
	private double _defaultPickToolDistance;
	private boolean _debugEnabled;
	
	public Config()
	{
		_paintFps = 30;
		_updateFps = 60;
		_tileWidth = 30;
		_tileHeight = 30;
		_defaultGravity = 70 * 9.81; // note: pixels/s^2 not m/s^2
		_defaultLemmingVelocity = 100;
		_defaultPlayerMotionSpeed = 150;
		_defaultPlayerJumpSpeed = 370;
		_defaultPickToolDistance = 50;
		_debugEnabled = false;
	}
	
	public int paintFps()
	{
		return _paintFps;
	}
	
	public int updateFps()
	{
		return _updateFps;
	}
	
	public int tileHeight()
	{
		return _tileHeight;
	}
	
	public int tileWidth()
	{
		return _tileWidth;
	}
	
	public double defaultGravity()
	{
		return _defaultGravity;
	}
	
	public double defaultLemmingVelocity()
	{
		return _defaultLemmingVelocity;
	}
	
	public double defaultPlayerMotionSpeed()
	{
		return _defaultPlayerMotionSpeed;
	}
	
	public double defaultPlayerJumpSpeed()
	{
		return _defaultPlayerJumpSpeed;
	}
	
	public double defaultPickToolDistance()
	{
		return _defaultPickToolDistance;
	}
	
	public boolean isDebugEnabled()
	{
		return _debugEnabled;
	}
	
	/// defines which levels are in the game.
	public void loadLevels()
	{
		LevelManager mgr = Game.instance().levelManager();
		
		try
		{
			LevelDemo lDemo = new LevelDemo();
			mgr.addLevel(lDemo);
			Level lZero = new LevelZero();
			mgr.addLevel(lZero);
			mgr.setDefaultLevel(lZero);
		}
		catch (Exception e)
		{
			System.out.println("Error loading level.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/// defines which keys the game responds to.
	public FlockKeyListener.Key keyForCode(int keyCode)
	{
		switch(keyCode)
		{
		case KeyEvent.VK_UP:
			return FlockKeyListener.Key.Jump;
		case KeyEvent.VK_DOWN:
			return FlockKeyListener.Key.PickUpItem;
		case KeyEvent.VK_LEFT:
			return FlockKeyListener.Key.Left;
		case KeyEvent.VK_RIGHT:
			return FlockKeyListener.Key.Right;
		case KeyEvent.VK_D:
			return FlockKeyListener.Key.Debug;
		case KeyEvent.VK_X:
			return FlockKeyListener.Key.UseItem;
		case KeyEvent.VK_Z:
			return FlockKeyListener.Key.TimeFreeze;
		case KeyEvent.VK_P:
		case KeyEvent.VK_ESCAPE:
			return FlockKeyListener.Key.Pause;
		case KeyEvent.VK_SPACE:
			return FlockKeyListener.Key.Action;
		default:
			return FlockKeyListener.Key.None;
		}
	}
}
