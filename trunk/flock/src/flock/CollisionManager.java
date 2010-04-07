package flock;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class CollisionManager
{
	/**
	 * Returns the rectangle of space where the entity can move,
	 * based on its current coordinates.  This is a rough method,
	 * and relies on being updated at every tick.
	 */
	public Rectangle2D.Double getSpace(Entity e)
	{
		final Tile[][] gameTiles = Game.instance().getTiles();
		final int tH = Game.instance().config().tileHeight(),
		          tW = Game.instance().config().tileWidth(),
		          tileRows = gameTiles.length,
		          tileCols = gameTiles[0].length;
		
		// Get the rectangle of tiles this entity is overlapping with.
		int minrow = Math.max(0, (int)(e._y / tH)),
		    maxrow = Math.min(tileRows - 1, (int)((e._y + e.height()) / tH)),
		    mincol = Math.max(0, (int)(e._x / tW)),
		    maxcol = Math.min(tileCols - 1, (int)((e._x + e.width()) / tW));
		//System.out.println("Entity " + e + " spans tile rows " +
		//		minrow + "-" + maxrow + ", tile cols " + mincol + "-" + maxcol + ".");
		
		// Look up, down, left, right on that rectangle and see how far the entity can go.
		// TODO: this can be precalculated for each tile (performance).
		Rectangle2D.Double ret = new Rectangle2D.Double();
		boolean stop;
		
		// Look up.
		stop = false;
		for(int row = minrow - 1; row >= 0 && !stop; row--)
			for(int col = mincol; col <= maxcol && !stop; col++)
				if(gameTiles[row][col].isSolid())
				{
					ret.y = tH * (row + 1);
					stop = true;
				}
		if(!stop)
		{
			System.err.println("Entity " + e + ": No wall looking up.");
			ret.y = 0;
		}
		
		// Look down.
		stop = false;
		for(int row = maxrow + 1; row < tileRows && !stop; row++)
			for(int col = mincol; col <= maxcol && !stop; col++)
				if(gameTiles[row][col].isSolid())
				{
					ret.height = tH * row - ret.y;
					stop = true;
				}
		if(!stop)
		{
			System.err.println("Entity " + e + ": No wall looking down.");
			ret.height = tH * tileRows - ret.y;
		}
		
		// Look left.
		stop = false;
		for(int col = mincol - 1; col >= 0 && !stop; col--)
			for(int row = minrow; row <= maxrow && !stop; row++)
				if(gameTiles[row][col].isSolid())
				{
					ret.x = tW * (col + 1);
					stop = true;
				}
		if(!stop)
		{
			System.err.println("Entity " + e + ": No wall looking left.");
			ret.x = 0;
		}
		
		// Look right.
		stop = false;
		for(int col = maxcol + 1; col < tileCols && !stop; col++)
			for(int row = minrow; row <= maxrow && !stop; row++)
				if(gameTiles[row][col].isSolid())
				{
					ret.width = tW * col - ret.x;
					stop = true;
				}
		if(!stop)
		{
			System.err.println("Entity " + e + ": No wall looking right.");
			ret.width = tW * tileCols - ret.x;
		}
		
		//System.out.println("Entity " + e + " has space " + ret);
		return ret;
	}
	
	//kill lemmings if two lemmings of opposite alignment collide
	public void correctLemmings()
	{
		ArrayList<LemmingEntity> lemmings = Game.instance().getLemmings();
		for (LemmingEntity lemming: lemmings)
		{
			if (!(lemming instanceof AntiLemmingEntity))
			{
				for (LemmingEntity anti : lemmings)
				{
					if (anti instanceof AntiLemmingEntity)
					{
						if (anti.intersects(lemming))
						{
							//System.out.println("They should have died");
							Game.instance().kill(anti);
							Game.instance().kill(lemming);
						}
					}
				}
				if (lemming.intersects(Game.instance().getDoor()))
				{
					Game.instance().kill(lemming);
					Game.instance().getDoor().enterLemming();
				}
			}
		}
	}
}
