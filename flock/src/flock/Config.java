package flock;

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
	
	public Config()
	{
		_paintFps = 30;
		_updateFps = 60;
		_tileWidth = 30;
		_tileHeight = 30;
		_defaultGravity = 10 * 9.81; // note: pixels/s^2 not m/s^2
		_defaultLemmingVelocity = 100;
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
	
	public void loadLevels()
	{
		LevelManager mgr = Game.instance().levelManager();
		
		try
		{
			LevelDemo lDemo = new LevelDemo();
			mgr.addLevel(lDemo);
			mgr.setDefaultLevel(lDemo);
		}
		catch (Exception e)
		{
			System.out.println("Error loading level.");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
