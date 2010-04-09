package flock;

import java.awt.Graphics2D;

public class AirTile extends Tile
{
	public AirTile()
	{
		super("null"); // no image
		_gravity = Game.instance().config().defaultGravity();
		_solid = false;
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y)
	{
	}
}
