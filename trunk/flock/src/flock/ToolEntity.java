package flock;

/**
 * A ToolEntity is an entity the player can pick up.
 * A ToolEntity is invisible while held by the player.
 */
abstract public class ToolEntity extends Entity
{
	public ToolEntity(String id, double x, double y)
	{
		super(id, x, y);
		//_accelY = 0; // don't fall
	}
	
	public void pickUp()
	{
		_active = false; // to prevent collision updates
	}
	
	public void drop()
	{
		_active = true;
	}
	
	
	
	abstract public void use();
}
