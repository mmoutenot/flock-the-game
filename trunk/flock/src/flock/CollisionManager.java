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
	
	/**
	 * Notify those entities that care about collisions if they
	 * collided with anything.
	 */
	public void notifyCollisions()
	{
		// TODO performance can be improved by precalculating most of this...
		ArrayList<Entity> colliders = new ArrayList<Entity>();
		for(Entity e: Game.instance().getEntities())
			if(e.caresAboutCollisions())// && !e.isFrozen())
				colliders.add(e);
		
		// Careful here not to send duplicate collided() messages.
		final int count = colliders.size();
		for(int a = 0; a < count; a++)
			for(int b = a + 1; b < count; b++)
				if(colliders.get(a).intersects(colliders.get(b)))
				{
					colliders.get(a).collided(colliders.get(b));
					colliders.get(b).collided(colliders.get(a));
				}
	}
	
	/**
	 * Checks any effects on each entity by the tile(s) it's on.
	 * (e.g. anti-gravity, spikes
	 */
	public void checkEnvironment()
	{
		ArrayList<Entity> entities = Game.instance().getEntities();
		ArrayList<Tile> tiles;
		
		for (Entity e : entities)
		{
			if(!e.isActive() || !e.isMoving())
				continue;
			
			tiles = e.getTiles();
			for (Tile t : tiles)
			{				
				if (t instanceof SpikeTile)
				{
					if (e instanceof LemmingEntity)
					{
						Game.instance().addToKillList((LemmingEntity) e);
					}
					else if (e instanceof PlayerEntity)
					{
						Game.instance().goToLevel(Game.instance().currentLevel().id());
					}
				}
			}
		}
	}
}
