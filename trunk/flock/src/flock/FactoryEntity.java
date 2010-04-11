package flock;

/**
 * An entity that produces lemmings or antilemmings.
 * 
 * TODO: make this generic
 */
public class FactoryEntity extends Entity
{
	private boolean _anti;
	private boolean _switchDirection;
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
		_switchDirection = false;
	}
	
	@Override
	protected void doUpdate(long msElapsed)
	{
		if(_count <= 0)
			return;
		
		_wait -= msElapsed;
		if(_wait <= 0)
		{
			final double x = _x + _rect.width / 2, y = _y + _rect.height / 2;
			Entity bastard = _anti ? new AntiLemmingEntity(x, y)
			                       : new LemmingEntity(x, y);
			bastard.setFrozen(false);
			if(_switchDirection)
				bastard.setVelX(-bastard.getVelX());
			_switchDirection = !_switchDirection;
			
			Game.instance().addToAddList(bastard);
			_wait = _delay;
			_count--;
		}
	}
}
