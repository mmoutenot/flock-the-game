package flock;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * The TerminalEntity allows the user to load a specific level by
 * inputting a password.
 */
public class TerminalEntity extends ActionEntity
{
	/// An overlay with a password input.
	private class TerminalOverlay extends Overlay
	{
		private final int _maxlen = 20;  
		private String _input;
		private boolean _failed;
		private long _failedTime;
		private final int _showFailedMessage = 1500; // milliseconds
		
		public TerminalOverlay(BufferedImage background)
		{
			super(background);
			_input = "";
		}
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			Game game = Game.instance();
			char c = e.getKeyChar();
			
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_ENTER:
			{
				Level level = game.levelManager().levelForPassword(_input);
				if(level == null) // no luck
				{
					_failed = true;
					_failedTime = System.currentTimeMillis();
					_input = "";
				}
				else
				{
					game.setPaused(false, null); // HACK to show Level's own overlay
					game.loadLevel(level);
				}
				break;
			}
			case KeyEvent.VK_ESCAPE:
				game.setPaused(false, null);
				break;
			case KeyEvent.VK_BACK_SPACE:
				if(_input.length() > 0)
					_input = _input.substring(0, _input.length() - 1);
				break;
			default:
				_failed = false;
				if(_input.length() < _maxlen && c != KeyEvent.CHAR_UNDEFINED)
					_input += c;
				break;
			}
		}

		@Override
		protected void doDraw(Graphics2D g)
		{
			final int W = Game.instance().getWidth();
			
			// Some text.
			// FIXME a lot of this menu stuff could be factored out in a MenuOverlay...
			final int messages = 3;
			final String[] message = {
					"To go to a specific level,",
			        "input its password below and press ENTER:",
			        "Or press ESCAPE to return to the game."
			};
			final int[] yposition = {
					100,
					130,
					300
			};
			Font font = new Font("Default", Font.PLAIN, 24); // TODO pick nicer font 
			g.setFont(font);
			g.setColor(Color.CYAN);
			for(int i = 0; i < messages; i++)
				g.drawString(message[i],
						(W - g.getFontMetrics().stringWidth(message[i])) / 2,
						yposition[i]);
			
			// The password input.
			final int pwdypos = 220,
			          pwdxspan = 250,
			          pwdyspan = 30;
			g.setColor(Color.GREEN);
			g.drawRect(W / 2 - pwdxspan, pwdypos - pwdyspan, pwdxspan * 2, pwdyspan * 2);
			g.drawString(_input,
					(W - g.getFontMetrics().stringWidth(_input)) / 2,
					pwdypos + 12); // the +12 is a HACK. FontMetrics.getHeight() is weird.
			
			// Message shown if password failed.
			if(_failed)
			{
				final String failmsg = "That didn't work!";
				if(System.currentTimeMillis() - _failedTime >= _showFailedMessage)
					_failed = false;
				else
				{
					g.setColor(Color.RED);
					g.drawString(failmsg,
							(W - g.getFontMetrics().stringWidth(failmsg)) / 2,
							pwdypos + 12);
				}
			}
		}
	}
	
	public TerminalEntity(double x, double y)
	{
		super("terminal", x, y);
	}

	@Override
	public void doUpdate(long msElapsed)
	{
	}
	
	@Override
	public void action()
	{
		Game game = Game.instance();
		game.setPaused(true, new TerminalOverlay(game.getImageForOverlay()));
	}
}
