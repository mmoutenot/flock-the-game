package flock;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * A Tile is a fixed-size, non-moving object on the screen.
 */
public class Tile implements Cloneable
{
	private String _id;
	protected Image _image;
	protected double _gravity;
	protected boolean _solid;
	protected int _height, _width;
	
	public Tile()
	{
		init("invalid");
	}
	
	public Tile(String id)
	{
		init(id);
	}
	
	/// "real" constructor
	private void init(String id)
	{
		_id = id;
		ImageManager mgr = Game.instance().imageManager();
		_image = mgr.getImage(id);
		_gravity = 0;
		_solid = true;
		//_height = _image.getHeight(null);
		//_width = _image.getWidth(null);
	}
	
	/**
	 * Required for deep-copying in Level.
	 * Not sure if there is a better way to do this.
	 * All subclasses must override this.
	 */
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			// WTF, shouldn't happen.
            throw new InternalError(e.toString());
        }
	}

	public String id()
	{
		return _id;
	}
	
	public double gravity()
	{
		return _gravity;
	}
	
	/// Mostly for testing...
	public void setGravity(double gravity)
	{
		_gravity = gravity;
	}
	
	public boolean isSolid()
	{
		return _solid;
	}
	
	public int height()
	{
		return _image.getHeight(null);
	}
	
	public int width()
	{
		return _image.getWidth(null);
	}
	
	/// override if you want to do something other than draw an image.
	public void draw(Graphics2D g, int x, int y)
	{
		g.drawImage(_image, x, y, null);
	}
}
