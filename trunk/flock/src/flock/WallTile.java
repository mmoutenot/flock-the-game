package flock;

public class WallTile extends Tile
{
	public WallTile()
	{
		super("wall");
		_accel = 0;
		_solid = true;
	}
}
