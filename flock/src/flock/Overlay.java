package flock;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * An Overlay is something that is drawn on top of the game screen
 * when the game is paused.  For example, the pause menu and the terminal
 * password input are overlays.
 * 
 * The overlay takes a background image, which it darkens and paints on
 * top of.  (The first attempt tried to darken the result of Game.draw() every
 * time without storing the background -- that was too slow.)
 */
abstract public class Overlay extends FlockKeyListener
{
	BufferedImage _background = null;
	
	/// Constructs an Overlay using background @p background.
	public Overlay(BufferedImage background)
	{
		_background = background;
		Graphics2D g = _background.createGraphics();
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.7f));
		g.fillRect(0, 0, _background.getWidth(), _background.getHeight());
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}
	
	/**
	 * Draws a nice transparency over the game, then calls doDraw()
	 * to draw the concrete Overlay (such as menu).
	 * Subclasses should implement doDraw().
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(_background, 0, 0, Color.LIGHT_GRAY, null);
		doDraw(g);
	}
	
	abstract protected void doDraw(Graphics2D g);
}
