package flock;

/**
 * Holds all configuration data for the game.
 * The only instance is created in Game.
 */
public class Config
{
	private int _paintFps;
	private int _updateFps;
	
	public Config()
	{
		// TODO be smarter than this.
		_paintFps = 30;
		_updateFps = 60;
	}
	
	public int paintFps()
	{
		return _paintFps;
	}
	
	public int updateFps()
	{
		return _updateFps;
	}
}
