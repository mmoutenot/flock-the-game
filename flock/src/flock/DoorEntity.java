package flock;

public class DoorEntity extends Entity
{
	private int _lemmingsThrough;
	public DoorEntity(double x, double y) 
	{
		super("door", x, y);
		_lemmingsThrough = 0;
	}
	
	public DoorEntity(String id, double x, double y)
	{
		super(id, x, y);
		_lemmingsThrough = 0;
	}
	
	public void init()
	{
		//doors don't move
		_velX = 0;
		_velY = 0;
		_accelX = 0;
		_accelY = 0;
	}
	
	public void enterLemming()
	{
		_lemmingsThrough++;
		System.out.println("Number of lemmings in door: " + _lemmingsThrough);
	}

	public void doUpdate(long msElapsed) 
	{
		;
	}

}
