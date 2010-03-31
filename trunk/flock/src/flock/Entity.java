package flock;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * An Entity is a moving object on the screen.
 * Unlike Tiles, Entities store their position.
 */
abstract public class Entity extends Tile
{
	protected Rectangle _rect;
	private long _lastTime;
	
	public Entity(String id)
	{
		super(id);
		_rect = new Rectangle(0, 0, _image.getWidth(null), _image.getHeight(null));
		update();
	}
	
	/// @return rectangle occupied by this Entity on screen.
	public Rectangle rect()
	{
		return _rect;
	}
	
	/// since _lastTime;
	public long elapsedTime()
	{
		return System.currentTimeMillis() - _lastTime;
	}
	
	/// updates the Entity. Subclasses should put update logic in doUpdate().
	public void update()
	{
		doUpdate(elapsedTime());
		_lastTime = System.currentTimeMillis();
	}
	
	/// subclasses must override / define.
	abstract public void doUpdate(long msElapsed);
	
	/// override if you want to do something other than draw an image.
	public void draw(Graphics2D g)
	{
		g.drawImage(_image, _rect.x, _rect.y, null);
	}
}
