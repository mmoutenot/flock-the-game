package flock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

abstract public class Level
{
	private String _id;
	private String _nextId;
	private String _title;
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
	
	public int rows()
	{
		return _rows;
	}
	
	public int cols()
	{
		return _cols;
	}
	
	/// returns a copy of the tile array
	public Tile[][] tiles()
	{
		Tile copy[][] = new Tile[_rows][_cols];
		for(int r = 0; r < _rows; r++)
			for(int c = 0; c < _cols; c++)
				copy[r][c] = (Tile) _tiles[r][c].clone();
		return copy;
	}
	
	/// returns a copy of the initial entities array
	public ArrayList<Entity> entities()
	{
		ArrayList<Entity> copy = new ArrayList<Entity>();
		for(Entity ent: _entities)
			copy.add((Entity) ent.clone());
		return copy;
	}
	
	/**
	 * Returns a copy of the PlayerEntity of this level.
	 * entities() must contain a single PlayerEntity.
	 */
	public PlayerEntity player()
	{
		for(Entity ent: _entities)
		{
			if(ent instanceof PlayerEntity)
				return (PlayerEntity)ent;
		}
		return null;
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
			System.out.println(line);
			
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
			else if(stuff[0].equals("next"))
				_nextId = stuff[1];
		}
		if(_rows == -1)
			throw new Exception("Level " + _id + ": missing rows=");
		if(_cols == -1)
			throw new Exception("Level " + _id + ": missing cols=");
		if(_title == null)
			throw new Exception("Level " + _id + ": missing title=");
		
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
				System.out.println(line);
				
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
				// ...
				}
			}
		}
		// FIXME should skip comments here too...
		line = reader.readLine();
		if(!line.equals("end"))
			throw new Exception("Level " + _id + ": extra lines or missing end.");
	}
}
