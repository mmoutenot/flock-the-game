package flock;

/// a demo entity displaying an image and moving to the right.
public class DemoEntity extends Entity
{
	public DemoEntity()
	{
		super("imageNotFound");
		
		_x=100;
		_y=100;
		_accelX = 0;
		_accelY = 10 * 9.81;
		_velX = 0;
		_velY = -100;
		update();
	}
	
	@Override
	public Object clone()
	{
		return super.clone();
	}

	@Override
	public void doUpdate(long msElapsed)
	{
		//System.out.println("x " + _x + " y " + _y + " velX " + _velX + " velY " + _velY
		//		+ " accelX " + _accelX + " accelY " + _accelY);
	}
}
