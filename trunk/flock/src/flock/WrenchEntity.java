package flock;

/**
 * An entity that builds bridges.
 */
public class WrenchEntity extends ToolEntity
{
	public WrenchEntity(double x, double y)
	{
		super("wrench", x, y);
	}

	@Override
	public void doUpdate(long msElapsed)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void use()
	{
		System.out.println("Wrench used at " + _x + ", " + _y);
		
		Tile[][] tiles = Game.instance().getTiles();
		int tileWidth = Game.instance().config().tileWidth();
		int tileHeight = Game.instance().config().tileHeight();
		int x = (int)_x;
		int y = (int)_y;
		
		//Go downward until we hit solid ground
		while (!tiles[y / tileHeight][x / tileWidth].isSolid())
		{
			y += tileHeight;
		}
		
		//Go right until we either go off the map or we hit empty space where the bridge can be built
		while ((x / tileWidth) < tiles[0].length && tiles[y / tileHeight][x / tileWidth].isSolid())
		{
			x += tileWidth;
		}
		
		//Build the bridge rightward until it hits solid ground.
		//Note: We might want to make it possible to build bridges leftwards too
		while ((x / tileWidth) < tiles[0].length && !tiles[y / tileHeight][x / tileWidth].isSolid())
		{
			tiles[y / tileHeight][x / tileWidth] = new BridgeTile(x - (x % tileWidth), y - (y % tileHeight));
			x += tileWidth; 
		}
		
		//Make it one-use?
	}
}
