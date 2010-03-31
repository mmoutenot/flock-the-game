package flock;

import java.util.HashMap;

/**
 * Contains information about all levels in existence.
 * The only instance is created in Game.
 */
public class LevelManager
{
	/// map id -> level
	private HashMap<String, Level> _levels;
	Level _defaultLevel;
	
	public LevelManager()
	{
		// No levels by default.
		_levels = new HashMap<String, Level>();
		_defaultLevel = null;
	}
	
	public void addLevel(Level level)
	{
		_levels.put(level.id(), level);
	}
	
	public void setDefaultLevel(Level level)
	{
		_defaultLevel = level;
	}
	
	public int levelCount()
	{
		return _levels.size();
	}
	
	public Level defaultLevel()
	{
		return _defaultLevel;
	}
	
	public Level level(String id)
	{
		return _levels.get(id);
	}
}
