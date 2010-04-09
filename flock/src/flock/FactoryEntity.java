package flock;

/**
 * An entity that produces lemmings or antilemmings.
 * 
 * TODO: make this generic
 */
public class FactoryEntity extends Entity
{
	private boolean _anti;
	private int _count;
	private long _delay;
	private long _wait;
	
	/**
	 * @param x, y -- coordinates.
	 * @param anti -- produce lemmings if false, antilemmings if true.
	 * @param count -- how many entities to produce.
	 * @param msDelay -- how long to wait until producing next one.
	 */
	public FactoryEntity(double x, double y, boolean anti, int count, long msDelay)
	{
		super("factory", x, y);
		_anti = anti;
		_count = count;
		_delay = msDelay;
		_wait = _delay;
		_moving = false;
	}
	
	@Override
	protected void doUpdate(long msElapsed)
	{
		if(_count <= 0)
			return;
		
		_wait -= msElapsed;
		if(_wait <= 0)
		{
			Entity bastard = _anti ? new AntiLemmingEntity(_x + 10, _y + 10)
			                       : new LemmingEntity(_x + 10, _y + 10);
			Game.instance().getEntities().add(0, bastard);
			_wait = _delay;
			_count--;
		}
	}
}
