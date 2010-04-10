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
	
	/// map password -> level
	private HashMap<String, Level> _passwords;
	
	/// the default level
	Level _defaultLevel;
	
	public LevelManager()
	{
		// No levels by default.
		_levels = new HashMap<String, Level>();
		_passwords = new HashMap<String, Level>();
		_defaultLevel = null;
	}
	
	public void addLevel(Level level)
	{
		_levels.put(level.id(), level);
		_passwords.put(level.password(), level);
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
	
	/// Returns fresh copy of level with id @p id.
	public Level level(String id)
	{
		try {
			return _levels.get(id).getClass().newInstance();
		} catch (Exception e) {
			System.out.println("Failed to create instance of level " + id);
			e.printStackTrace();
			return null;
		}
	}
	
	/// Returns fresh copy of this level.
	public Level level(Level level)
	{
		return level(level.id());
	}
	
	/**
	 * @return a fresh copy of the Level corresponding to this password,
	 *         or null if no match.
	 */
	public Level levelForPassword(String password)
	{
		return level(_passwords.get(password));
	}
}
