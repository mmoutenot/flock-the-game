package flock;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * A Tile is a fixed-size, non-moving object on the screen.
 */
public class Tile
{
	private String _id;
	protected Image _image;
	protected double _accel;
	protected boolean _solid;
	
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
		_accel = 0;
		_solid = true;
	}

	public String id()
	{
		return _id;
	}
	
	public double acceleration()
	{
		return _accel;
	}
	
	public boolean isSolid()
	{
		return _solid;
	}
	
	/// override if you want to do something other than draw an image.
	public void draw(Graphics2D g, int x, int y)
	{
		g.drawImage(_image, x, y, null);
	}
}
