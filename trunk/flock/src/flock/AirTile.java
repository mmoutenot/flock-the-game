package flock;

import java.awt.Graphics2D;

public class AirTile extends Tile
{
	public AirTile()
	{
		super(); // no image
		_accel = Game.instance().config().defaultAcceleration();
		_solid = false;
	}
	
	@Override
	public Object clone()
	{
		return super.clone();
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y)
	{
	}
}
