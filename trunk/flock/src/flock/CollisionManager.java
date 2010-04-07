package flock;

import java.util.ArrayList;

public class CollisionManager 
{
	public CollisionManager(Game g)
	{
	}
	
	public Tile[] getEntityTiles(Entity e)
	{
		Tile[] result = new Tile[4];
		
		result[0] = Game.instance().getTiles()[(int)(e._y / Game.instance().config().tileHeight())][(int)(e._x / Game.instance().config().tileWidth())];
		result[1] = Game.instance().getTiles()[(int)(e._y / Game.instance().config().tileHeight())][(int)((e._x + e.width()) / Game.instance().config().tileWidth())];
		result[2] = Game.instance().getTiles()[(int)((e._y + e.height()) / Game.instance().config().tileHeight())][(int)(e._x / Game.instance().config().tileWidth())];
		result[3] = Game.instance().getTiles()[(int)((e._y + e.height()) / Game.instance().config().tileHeight())][(int)((e._x + e.width()) / Game.instance().config().tileWidth())];
		
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
			}
		}
	}
}
