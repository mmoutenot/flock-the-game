package flock;

public class BridgeTile extends Tile
{
	public BridgeTile(double x, double y)
	{
		super("bridge");
		_gravity = 0;
		_solid = true;
	}
}
