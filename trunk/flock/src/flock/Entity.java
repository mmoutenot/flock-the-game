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
	/*
	 * the following variables are physics and position related
	 */
	protected double _x;
	protected double _y;
	protected double _velX;
	protected double _velY;
	protected double _accelX;
	protected double _accelY;
	public double gravity = .1;
	
	
	public Entity(String id)
	{
		super(id);
		_rect = new Rectangle(0, 0, _image.getWidth(null), _image.getHeight(null));
		update();
		//physics-related variables
		_x=100;
		_y=100;
		_accelX = 0.001;
		_accelY = 0;
		_velX = 0;
		_velY = -5;
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
	
	public void setX(int x)
	{
		_rect.x = x;
	}
	
	public void setY(int y)
	{
		_rect.y = y;
	}
	
	/// since _lastTime;
	public long elapsedTime()
	{
		return System.currentTimeMillis() - _lastTime;
	}
	
	/// updates the Entity. Subclasses should put update logic in doUpdate().
	public void update()
	{
		_velX +=_accelX;
		_velY +=(-_accelY+gravity);
		_x = _x+_velX;
		_y = _y+_velY;
		_rect.x=(int) _x;
		_rect.y=(int) _y;
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
	/*
	 * physics and position related functions
	 */
	///this function zeroes out acceleration in a given dimension. axis = 1 for x axis, 2 for y axis, or 0 for both
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
	
	public void changeAccelX(double newAccelX)
	{
		_accelX = newAccelX;
	}
	public void changeAccelY(double newAccelY)
	{
		_accelY = newAccelY;
	}
}
