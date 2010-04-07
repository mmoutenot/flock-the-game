package flock;

import java.util.ArrayList;

public class CollisionManager 
{
	public Tile[] getEntityTiles(Entity e)
	{
		final Tile[][] gameTiles = Game.instance().getTiles();
		final int tH = Game.instance().config().tileHeight(),
		          tW = Game.instance().config().tileWidth(),
		          tileRows = gameTiles.length,
		          tileCols = gameTiles[0].length;
		
		// Get the coordinates of the tiles this entity is overlapping with.
		// FIXME: assumes there are exactly four of them.
		int[][] tc = new int[4][2];
		tc[0][0] = (int)(e._y / tH);
		tc[0][1] = (int)(e._x / tW);
		tc[1][0] = tc[0][0];
		tc[1][1] = (int)((e._x + e.width()) / tW);
		tc[2][0] = (int)((e._y + e.height()) / tH);
		tc[2][1] = tc[0][1];
		tc[3][0] = tc[2][0];
		tc[3][1] = tc[1][1];
		
		// Adjust coords in case entity is partly off-screen.
		for(int i = 0; i < 4; i++)
		{
			tc[i][0] = Math.max(tc[i][0], 0);
			tc[i][0] = Math.min(tc[i][0], tileRows - 1);
			tc[i][1] = Math.max(tc[i][1], 0);
			tc[i][1] = Math.min(tc[i][1], tileCols - 1);
		}
		System.out.println("Entity at " + e._x + " x " + e._y + " has tiles:");
		System.out.println("       " + tc[0][0] + "x" + tc[0][1] + ", " +
				                       tc[1][0] + "x" + tc[1][1] + ", " +
				                       tc[2][0] + "x" + tc[2][1] + ", " +
				                       tc[3][0] + "x" + tc[3][1] + ".");
		
		Tile[] result = new Tile[4];
		result[0] = gameTiles[tc[0][0]][tc[0][1]];
		result[1] = gameTiles[tc[1][0]][tc[1][1]];
		result[2] = gameTiles[tc[2][0]][tc[2][1]];
		result[3] = gameTiles[tc[3][0]][tc[3][1]];
		return result;
	}
	
	public void correctWalls(Entity e)
	{
		Tile[] tiles = getEntityTiles(e);
		
		if ((tiles[2] instanceof WallTile || tiles[3] instanceof WallTile) && 
				!(tiles[0] instanceof WallTile || tiles[1] instanceof WallTile))
		{
			e.setLowerWall(true);
			
		}
		else if ((tiles[2] instanceof WallTile && tiles[3] instanceof WallTile))
		{
			e.setLowerWall(true);
		}
		else
		{
			e.setLowerWall(false);
			if (e instanceof PlayerEntity)
				((PlayerEntity)e).setJumping(true);
		}
		
		if ((tiles[1] instanceof WallTile || tiles[3] instanceof WallTile) && !e.againstLowerWall())
		{
			e.setRightWall(true);
			e.setVelX(0);
		}
		else if ((tiles[1] instanceof WallTile && tiles[3] instanceof WallTile))
		{
			e.setRightWall(true);
			e.setVelX(0);
		}
		else
		{
			e.setRightWall(false);
		}
		
		if ((tiles[0] instanceof WallTile || tiles[2] instanceof WallTile) && !e.againstLowerWall())
		{
			e.setLeftWall(true);
			e.setVelX(0);
		}
		else if ((tiles[0] instanceof WallTile && tiles[2] instanceof WallTile))
		{
			e.setLeftWall(true);
			e.setVelX(0);
		}
		else
		{
			e.setLeftWall(false);
		}
		
		if ((tiles[0] instanceof WallTile || tiles[1] instanceof WallTile) && e.getVelY() < 0)
		{
			if (!(e.againstLeftWall() || e.againstRightWall()) || (e.againstLeftWall() && e.againstRightWall()))
			{
				e.setUpperWall(true);
			}
			if (!((tiles[0] instanceof WallTile && tiles[2] instanceof WallTile) ||
					(tiles[1] instanceof WallTile && tiles[3] instanceof WallTile)))
			{
				e.setUpperWall(true);
			}
		}
		else
		{
			e.setUpperWall(false);
		}
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
