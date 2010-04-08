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
		
		while (!tiles[x / tileWidth][y / tileHeight].isSolid())
		{
			System.out.println("x = " + x / tileWidth + ", y = " + y / tileHeight);
			y += tileHeight;
		}
		
		System.out.println("x = " + x / tileWidth + ", y = " + y / tileHeight);
		
		while (tiles[x / tileWidth][y / tileHeight].isSolid())
		{
			x += tileWidth;
			//System.out.println("x = " + x / tileWidth + ", y = " + y / tileHeight + " " + tiles[x / tileWidth][y / tileHeight].isSolid());
		}
		
		System.out.println("x = " + x + ", y = " + y);
		
		while (!tiles[x / tileWidth][y / tileHeight].isSolid())
		{
			tiles[x / tileWidth][y / tileHeight] = new BridgeTile(x - (x % tileWidth), y - (y % tileHeight));
			x += tileWidth; 
		}
	}
}
