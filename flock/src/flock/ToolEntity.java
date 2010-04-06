package flock;

import java.awt.Graphics2D;

/**
 * A ToolEntity is an entity the player can pick up.
 * A ToolEntity is invisible while held by the player.
 */
abstract public class ToolEntity extends Entity
{
	protected boolean _visible;
	
	public ToolEntity(String id, double x, double y)
	{
		super(id, x, y);
		_visible = true;
		_accelY = 0;
	}
	
	/// Draw only if not held by the user.
	@Override
	public void draw(Graphics2D g)
	{
		if(_visible)
			super.draw(g);
	}
	
	public void pickUp()
	{
		_visible = false;
	}
	
	public void drop()
	{
		_visible = true;
	}
	
	public void doUpdate()
	{
		
	}
	
	abstract public void use();
}
