package flock;

/// An entity representing a lemming.
public class LemmingEntity extends Entity
{
	private boolean _headedLeft;
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
		_headedLeft = false;
	}

	public void doUpdate(long msElapsed)
	{
		if (againstLowerWall() && _headedLeft)
		{
			if (againstLeftWall())
			{
				_velX = Game.instance().config().defaultLemmingVelocity();
				_headedLeft = false;
			}
			else
			{
				_velX = -Game.instance().config().defaultLemmingVelocity();
			}
		}
		else if (againstLowerWall() && !_headedLeft)
		{
			if (againstRightWall())
			{
				_velX = -Game.instance().config().defaultLemmingVelocity();
				_headedLeft = true;
			}
			else
			{
				_velX = Game.instance().config().defaultLemmingVelocity();
			}
		}
	}
}
