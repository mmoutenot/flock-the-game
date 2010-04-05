package flock;

/// An entity representing the player.
public class PlayerEntity extends Entity
{
	public PlayerEntity(double x, double y)
	{
		super("player", x, y);
		
		// TODO move this to Entity once we have collision detection with walls.
		_accelY = Game.instance().config().defaultGravity();
	}
	
	@Override
	public void doUpdate(long msElapsed)
	{
		// TODO once we have collision with walls
		
		// for now:
		if(_y > 500)
		{
			_velY = 0;
			_accelY = 0;
		}
		else
		{
			_accelY = Game.instance().config().defaultGravity();
		}
	}
}
