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
	}

	@Override
	public void doUpdate(long msElapsed)
	{
		// TODO Auto-generated method stub

	}
}
