package flock;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * Stub class for a game entity.
 */
abstract public class Entity
{
	protected Rectangle _rect;
	protected Image _image;
	private long _lastTime;
	
	/// subclasses should call super constructor.
	public Entity(String imageName)
	{
		ImageManager mgr = Game.instance().imageManager();
		Image img = mgr.getImage(imageName);
		Rectangle rect = new Rectangle(0, 0, img.getWidth(null), img.getHeight(null));
		init(rect, img);
	}
	
	private void init(Rectangle rect, Image image)
	{
		_rect = rect;
		_image = image;
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
	
	/// override if you want to do something other than draw an image.
	public void draw(Graphics2D g)
	{
		g.drawImage(_image, _rect.x, _rect.y, null);
	}
	
	/// subclasses must override / define.
	abstract public void doUpdate(long msElapsed);
}
