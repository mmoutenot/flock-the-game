package flock;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/// Pause menu.
public class PauseOverlay extends Overlay
{
	static private enum Entry
	{
		UnPause			("Return to game"),
		RestartLevel	("Restart level"),
		GoToLevel0		("Go to level 0"),
		QuitGame		("Quit game");
		
		public final String message;
		Entry(String msg)
		{
			message = msg;
		}
	};
	final static private int _entries = Entry.values().length;
		
	private int _activeEntry;
	
	public PauseOverlay(BufferedImage background)
	{
		super(background);
		_activeEntry = 0;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_DOWN:
			_activeEntry = (_activeEntry + 1) % _entries;
			break;
		case KeyEvent.VK_UP:
			_activeEntry = (_activeEntry + _entries - 1) % _entries;
			break;
		case KeyEvent.VK_P:
		case KeyEvent.VK_ESCAPE: // assume UnPause
			_activeEntry = Entry.UnPause.ordinal();
			accept();
			break;
		case KeyEvent.VK_ENTER:
			accept();
			break;
		}
	}

	@Override
	protected void doDraw(Graphics2D g)
	{
		final int W = Game.instance().getWidth(),
                  H = Game.instance().getHeight();
		final String paused = "PAUSED";
		int starty = 100;
		
		// Menu title.
		Font font = new Font("Default", Font.PLAIN, 36); // TODO pick nicer font 
		g.setFont(font);
		g.setColor(Color.CYAN);
		g.drawString(paused, (W - g.getFontMetrics().stringWidth(paused)) / 2, starty);
		starty += 100;
		
		// Menu entries.
		font = font.deriveFont(Font.PLAIN, 24);
		g.setFont(font);
		
		for(Entry e: Entry.values())
		{
			final boolean active = (e.ordinal() == _activeEntry);
			g.setColor(active ? Color.RED : Color.GREEN);
			g.drawString(e.message, (W - g.getFontMetrics().stringWidth(e.message)) / 2, starty);
			starty += 70;
		}
	}
	
	/// Performs the action selected in the menu.
	private void accept()
	{
		Game game = Game.instance();
		switch(Entry.values()[_activeEntry])
		{
		case UnPause:
			game.setPaused(false, null);
			break;
		case RestartLevel:
			game.loadLevel(game.levelManager().level(game.currentLevel()));
			game.setPaused(false, null);
			break;
		case GoToLevel0:
			game.loadLevel(game.levelManager().defaultLevel());
			game.setPaused(false, null);
			break;
		case QuitGame:
			System.exit(0); // not very elegant...
			break;
		}
	}
}
