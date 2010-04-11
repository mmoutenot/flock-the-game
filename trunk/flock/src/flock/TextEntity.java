package flock;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/// An entity displaying some text.
public class TextEntity extends Entity
{
	/**
	 * We are setting Entity._active = false to avoid drawing the null image.
	 * So let's have a visible attribute.
	 */
	private boolean _visible;
	private String[] _lines;
	
	/**
	 * Creates a TextEntity at coordinates @p x, @p y,
	 * with width @p w and height @p h.
	 * Use setLines() to set the text to display.
	 */
	public TextEntity(double x, double y, double w, double h)
	{
		super("null", x, y); // no image
		init(x, y, w, h, null);
	}
	
	/**
	 * Creates a TextEntity at coordinates @p x, @p y,
	 * with width @p w and height @p h, showing text lines @l.
	 */
	public TextEntity(double x, double y, double w, double h, String[] l)
	{
		super("null", x, y); // no image
		init(x, y, w, h, l);
	}

	private void init(double x, double y, double w, double h, String[] l)
	{
		_rect.x = (int)x;
		_rect.y = (int)y;
		_rect.width = (int)w;
		_rect.height = (int)h;
		_lines = l;
		_moving = false; // don't move
		_collide = false; // don't care about collisions
		_visible = true;
	}
	
	public void setLines(String[] lines)
	{
		_lines = lines;
	}
	
	public void setVisible(boolean visible)
	{
		_visible = visible;
	}

	@Override
	protected void doUpdate(long msElapsed)
	{
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if(!_visible)
			return;
		
		// Outer border
		g.setColor(Color.BLACK);
		g.drawRect(_rect.x, _rect.y, _rect.width, _rect.height);
		
		// Inner background
		g.setColor(Color.WHITE);
		g.fillRect(_rect.x + 1, _rect.y + 1, _rect.width - 1, _rect.height - 1);
		
		// Text
		g.setColor(Color.BLACK);
		final FontMetrics fm = g.getFontMetrics();
		int y = _rect.y + fm.getHeight();
		for(String s: _lines)
		{
			g.drawString(s, _rect.x + (_rect.width - fm.stringWidth(s)) / 2, y);
			y += fm.getHeight();
		}
	}
}
