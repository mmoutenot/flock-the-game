package flock;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * An Entity is a moving object on the screen.
 * Unlike Tiles, Entities store their position.
 */
abstract public class Entity extends Tile
{
	protected Rectangle _rect;
	private long _lastTime;
	
	/*
	 * The following variables are physics and position related.
	 * Note about directions: X=0,Y=0 is the top-left corner. Positive directions
	 * are to the right and downwards, respectively.
	 */
	protected double _x;
	protected double _y;
	protected double _velX;
	protected double _velY;
	protected double _accelX;
	protected double _accelY;
	protected boolean _frozen;
	
	public Entity(String id)
	{
		super(id);
		init(0, 0);
	}
	
	public Entity(String id, double x, double y)
	{
		super(id);
		init(x, y);
	}
	
	/// "real" constructor (to avoid duplication)
	private void init(double x, double y)
	{
		_rect = new Rectangle((int)x, (int)y, _image.getWidth(null), _image.getHeight(null));
		_x = x;
		_y = y;
		_velX = _velY = 0;
		_accelX = _accelY = 0;
		_frozen = false;
		update();
	}
	
	/**
	 * Required for deep-copying in Level.
	 * Not sure if there is a better way to do this.
	 * All subclasses must override this.
	 */
	@Override
	public Object clone()
	{
		Entity copy = (Entity) super.clone();
		copy._rect = (Rectangle) _rect.clone();
		return copy;
	}
	
	/// @return rectangle occupied by this Entity on screen.
	public Rectangle rect()
	{
		return _rect;
	}
	
	public void setX(double x)
	{
		_x = x;
		update();
	}
	
	public void setY(double y)
	{
		_y = y;
		update();
	}
	
	public void setVelX(double vel)
	{
		_velX = vel;
		update();
	}
	
	public void setVelY(double vel)
	{
		_velY = vel;
		update();
	}
	
	public void setAccelX(double newAccelX)
	{
		_accelX = newAccelX;
		update();
	}
	
	public void setAccelY(double newAccelY)
	{
		_accelY = newAccelY;
		update();
	}
	
	public void setFrozen(boolean frozen)
	{
		_frozen = frozen;
		update();
	}
	
	/// since _lastTime;
	public long elapsedTime()
	{
		return System.currentTimeMillis() - _lastTime;
	}
	
	public Point2D.Double center()
	{
		return new Point2D.Double(_x + _rect.width, _y + _rect.height);
	}
	
	/// Distance from my center to other Entity's center.
	public double distance(Entity other)
	{
		return center().distance(other.center());
	}
	
	/**
	 * Updates the Entity's position and velocity.
	 * Subclasses should put update logic in doUpdate().
	 */
	public void update()
	{
		final long ms = elapsedTime();
		final double sec = ms / 1000.0;
		
		_rect.x = (int)_x;
		_rect.y = (int)_y;
		if(!_frozen)
		{
			_velX += _accelX * sec;
			_velY += _accelY * sec;
			_x += _velX * sec;
			_y += _velY * sec;
		}
		
		doUpdate(ms);
		_lastTime = System.currentTimeMillis();
	}
	
	/// subclasses must override / define.
	abstract public void doUpdate(long msElapsed);
	
	/// override if you want to do something other than draw an image.
	public void draw(Graphics2D g)
	{
		g.drawImage(_image, _rect.x, _rect.y, null);
	}
	
	/// zeroes out acceleration in a given dimension. axis = 1 for x axis, 2 for y axis, or 0 for both
	public void stopMovement(int axis)
	{
		if(axis==0)
		{
			_accelX=0;
			_accelY=0;
			_velX=0;
			_velY=0;
		}
		if(axis==1)
		{
			_accelX=0;
			_velX=0;
		}
		if(axis==2)
		{
			_accelY=0;
			_velY=0;
		}
	}
}
