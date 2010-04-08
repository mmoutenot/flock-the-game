package flock;

/**
 * An entity representing the player.
 * The player can hold at most one ToolEntity at a time.
 */
public class PlayerEntity extends Entity
{
	private ToolEntity _tool = null;
	
	public PlayerEntity(double x, double y)
	{
		super("player", x, y);
		_collide = true;
	}

	public void doUpdate(long msElapsed)
	{
		// If we have a tool, update its position to match ours.
		if(_tool != null)
		{
			_tool.setX(_x);
			_tool.setY(_y);
		}
	}
	
	/// Returns currently held tool, or null if none.
	public ToolEntity tool()
	{
		return _tool;
	}
	
	/// Drops the current tool at the current location.
	public void dropTool()
	{
		if(_tool == null)
			return;
		_tool.drop();
		_tool = null;
	}
	
	/// Picks up a tool. Drops current tool if holding one.
	public void pickTool(ToolEntity tool)
	{
		dropTool();
		_tool = tool;
		_tool.pickUp();
		doUpdate(0); // syncs positions
	}
	
	/// Uses current tool.
	public void useTool()
	{
		if(_tool == null)
			return;
		_tool.use();
		// TODO figure out what to do about number of uses. Probably make tools single-use.
	}
	
	/// Jumps if it's on top of solid ground.
	public void tryJump()
	{
		if(_space.y + _space.height - _y < 3)
			setVelY(-Game.instance().config().defaultPlayerJumpSpeed());
	}
}
