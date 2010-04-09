package flock;

/// An entity representing a lemming.
public class LemmingEntity extends Entity
{
	public LemmingEntity(double x, double y)
	{
		super("lemming", x, y);
		init();
	}
	
	/// additional constructor to allow custom lemmings (in our case just anti-lemmings).
	protected LemmingEntity(String id, double x, double y)
	{
		super(id, x, y);
		init();
	}
	
	/// "real" constructor (to avoid duplication)
	private void init()
	{
		_velX = Game.instance().config().defaultLemmingVelocity();
		_collide = true;
	}

	/// Toggle direction of lemming when it hits something solid.
	@Override
	public void doUpdate(long msElapsed)
	{
		if(_x - _space.x < 2 || _space.x + _space.width - _x < 2)
			setVelX(-_velX);
	}
	
	public void kill()
	{
		// NOTE: Removing the entity from Game._entities here causes problems
		// because things may be iterating through the _entities right now.
		// So just make it inactive for now.
		_active = false;
		_image = null;
		_velX = _velY = _accelX = _accelY = 0;
		_collide = false;
	}
}
