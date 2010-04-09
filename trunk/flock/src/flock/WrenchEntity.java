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
		
		int facing = 1;
		
		if (Game.instance().player().facingLeft())
		{
			facing = -1;
			x += Game.instance().player().width();
		}
		
		//Go downward until we hit solid ground
		while (!tiles[y / tileHeight][x / tileWidth].isSolid())
		{
			y += tileHeight;
		}
		
		//Go left/right until we either go off the map or we hit empty space where the bridge can be built
		while ((x / tileWidth) < tiles[0].length && (x / tileWidth) >= 0 && tiles[y / tileHeight][x / tileWidth].isSolid())
		{
			x += tileWidth * facing;
		}
		
		//Build the bridge rightward or leftward until it hits solid ground.
		//Note: We might want to make it possible to build bridges leftwards too
		while ((x / tileWidth) < tiles[0].length && (x / tileWidth) >= 0 && !tiles[y / tileHeight][x / tileWidth].isSolid())
		{
			tiles[y / tileHeight][x / tileWidth] = new BridgeTile(x - (x % tileWidth), y - (y % tileHeight));
			x += tileWidth * facing; 
		}
		
		//Make it one-use?
	}
}
