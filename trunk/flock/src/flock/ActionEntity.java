package flock;

/**
 * An ActionEntity is an entity that can perform an action,
 * if it is "activated" by the player.
 * ActionEntity cannot be picked up, like ToolEntities.
 */
abstract public class ActionEntity extends Entity
{
	public ActionEntity(String id, double x, double y)
	{
		super(id, x, y);
	}
	
	abstract public void action();
}
