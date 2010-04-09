package flock;

/// An entity representing an anti-lemming.
public class AntiLemmingEntity extends LemmingEntity
{
	public AntiLemmingEntity(double x, double y)
	{
		super("antilemming", x, y);
	}
	
	@Override
	protected void collided(Entity other)
	{
		if(other instanceof LemmingEntity)
		{
			Game.instance().addToKillList(this);
			Game.instance().addToKillList((LemmingEntity) other);
		}
	}
}
