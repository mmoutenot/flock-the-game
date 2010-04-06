package flock;

import java.util.ArrayList;

public class CollisionManager 
{
	private Game _game;
	
	public CollisionManager(Game g)
	{
		_game = g;
	}
	
	public Tile[] getEntityTiles(Entity e)
	{
		Tile[] result = new Tile[4];
		
		result[0] = _game.getTiles()[9][9];
		result[0] = _game.getTiles()[(int)(e._y / _game.config().tileHeight())][(int)(e._x / _game.config().tileWidth())];
		result[1] = _game.getTiles()[(int)(e._y / _game.config().tileHeight())][(int)((e._x + e.width()) / _game.config().tileWidth())];
		result[2] = _game.getTiles()[(int)((e._y + e.height()) / _game.config().tileHeight())][(int)(e._x / _game.config().tileWidth())];
		result[3] = _game.getTiles()[(int)((e._y + e.height()) / _game.config().tileHeight())][(int)((e._x + e.width()) / _game.config().tileWidth())];
		
		return result;
	}
	
	public void correctWalls(Entity e)
	{
		Tile[] tiles = getEntityTiles(e);
		
		if ((tiles[0] instanceof WallTile || tiles[1] instanceof WallTile) && e.getVelY() < 0)
		{
			e.setUpperWall(true);
		}
		else
		{
			e.setUpperWall(false);
		}
		
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
	}
	
	//kill lemmings if two lemmings of opposite alignment collide
	public void correctLemmings()
	{
		ArrayList<LemmingEntity> lemmings = _game.getLemmings();
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
							_game.kill(anti);
							_game.kill(lemming);
						}
					}
				}
			}
		}
	}
}
