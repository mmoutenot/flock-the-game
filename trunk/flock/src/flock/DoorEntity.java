package flock;

/// A door through which LemmingEntities can go through.
public class DoorEntity extends ActionEntity
{
	private int _lemmingsThrough;
	private int _lemmingsRequired;
	private String _nextLevel; //having this in the door class allows us to have multiple doors in the same room
	
	public DoorEntity(double x, double y, int lemmings, String next) 
	{
		super("door", x, y);
		_lemmingsThrough = 0;
		_lemmingsRequired = lemmings;
		_nextLevel = next;
		
		_collide = true;
		//doors don't move
		_velX = 0;
		_velY = 0;
		_accelX = 0;
		_accelY = 0;
	}

	@Override
	public void doUpdate(long msElapsed) 
	{
		;
	}
	
	@Override
	protected void collided(Entity other)
	{
		if(other instanceof LemmingEntity &&
		   !(other instanceof AntiLemmingEntity)) // HACK
		{
			Game.instance().addToKillList((LemmingEntity)other);
			_lemmingsThrough++;
			System.out.println("Number of lemmings in door: " + _lemmingsThrough);
		}
	}
	
	@Override
	public void action() 
	{
		if (_lemmingsThrough >= _lemmingsRequired)
		{
			Game.instance().goToLevel(_nextLevel);
		}
	}
}
