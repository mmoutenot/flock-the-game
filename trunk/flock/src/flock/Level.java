package flock;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * A level for the game.
 * Subclasses should:
 * 1) Implement a constructor that calls the super constructor with the
 *    appropriate string id (which loads the tiles from the appropriate file)
 * 2) Create entities and add them to _entities. Entities are drawn in the order
 *    they are found in _entities, so the PlayerEntity should be added last.
 */
abstract public class Level
{
	/**
	 * A basic Overlay for the level, showing its title
	 * and password.
	 */
	protected class SimpleOverlay extends Overlay
	{
		private Level _level;
		
		public SimpleOverlay(BufferedImage background, Level level)
		{
			super(background);
			_level = level;
		}
		
		public void keyPressed(KeyEvent e)
		{
			Game.instance().setPaused(false, null);
		}

		@Override
		protected void doDraw(Graphics2D g)
		{
			final int W = Game.instance().getWidth();

			// Some text.
			// FIXME a lot of this menu stuff could be factored out in a MenuOverlay...
			final int messages = 5;
			final String[] message = {
					"Level Title:",
			        _level._title,
			        "Level password:",
			        _level._password,
			        "Press any key to continue..."
			};
			final int[] yposition = {
					100,
					140,
					200,
					240,
					300
			};
			final Color[] color = {
					Color.CYAN,
					Color.GREEN,
					Color.CYAN,
					Color.RED,
					Color.GREEN
			};
			Font font = new Font("Default", Font.PLAIN, 24); // TODO pick nicer font 
			g.setFont(font);
			for(int i = 0; i < messages; i++)
			{
				g.setColor(color[i]);
				g.drawString(message[i],
						(W - g.getFontMetrics().stringWidth(message[i])) / 2,
						yposition[i]);
			}
		}
	}
	
	private String _id;
	private String _nextId;
	private String _title;
	private String _password;
	private int _rows, _cols;
	protected Tile[][] _tiles;
	protected ArrayList<Entity> _entities;
	
	/// subclasses should call super constructor
	public Level(String id) throws Exception
	{
		_rows = _cols = -1;
		_id = id;
		read();
		_entities = new ArrayList<Entity>();
	}
	
	public String id()
	{
		return _id;
	}
	
	public String nextId()
	{
		return _nextId;
	}
	
	public String title()
	{
		return _title;
	}
	
	public String password()
	{
		return _password;
	}
	
	public int rows()
	{
		return _rows;
	}
	
	public int cols()
	{
		return _cols;
	}
	
	public Tile[][] tiles()
	{
		return _tiles;
	}
	
	public ArrayList<Entity> entities()
	{
		return _entities;
	}
	
	/// Reads the tile info from disk.
	// FIXME: unsure where to best handle exceptions.
	public void read() throws Exception
	{
		String line;
		
		URL url = Game.instance().getClass().getClassLoader().
						getResource("flocklevel/" + _id + ".lvl");
		InputStream in = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		// Read header
		while((line = reader.readLine()) != null)
		{
			//System.out.println(line);
			
			if(line.startsWith(";"))
				continue;
			if(line.equals("begin"))
				break;
			String[] stuff = line.split("=", 2);
			if(stuff.length != 2)
				throw new Exception("Cannot parse line \"" + line + "\" in level " + _id);
			if(stuff[0].equals("rows"))
				_rows = Integer.parseInt(stuff[1]);
			else if(stuff[0].equals("columns"))
				_cols = Integer.parseInt(stuff[1]);
			else if(stuff[0].equals("title"))
				_title = stuff[1];
			else if(stuff[0].equals("password"))
				_password = stuff[1];
			else if(stuff[0].equals("next"))
				_nextId = stuff[1];
		}
		if(_rows == -1)
			throw new Exception("Level " + _id + ": missing rows=");
		if(_cols == -1)
			throw new Exception("Level " + _id + ": missing cols=");
		if(_title == null)
			throw new Exception("Level " + _id + ": missing title=");
		if(_password == null)
			throw new Exception("Level " + _id + ": missing password=");
		
		// Read level content.
		if(!line.equals("begin"))
			throw new Exception("Level " + _id + ": missing begin");
		_tiles = new Tile[_rows][_cols];
		for(int r = 0; r < _rows; r++)
		{
			// Skip comments.
			while(true)
			{
				line = reader.readLine();
				//System.out.println(line);
				
				if(line == null)
					throw new Exception("Level " + _id + ": expected " + _rows +
							            " rows, but only found " + r + ".");
				if(!line.startsWith(";"))
					break;
			}
			
			// Read tiles.
			if(line.length() != _cols)
				throw new Exception("Level " + _id + " row " + r + ": expected " + _cols +
						            " columns, but only found " + line.length() + ".");
			for(int c = 0; c < _cols; c++)
			{
				switch(line.charAt(c))
				{
				case ' ': _tiles[r][c] = new AirTile(); break;
				case '#': _tiles[r][c] = new WallTile(); break;
				case '^': _tiles[r][c] = new SpikeTile(); break;
				// ...
				}
			}
		}
		// FIXME should skip comments here too...
		line = reader.readLine();
		if(!line.equals("end"))
			throw new Exception("Level " + _id + ": extra lines or missing end.");
	}
	
	/**
	 * Returns an overlay to use as intro to this level.
	 * Override if you want something fancier than SimpleOverlay.
	 */
	public Overlay getOverlay(BufferedImage background)
	{
		return new SimpleOverlay(background, this);
	}
}
