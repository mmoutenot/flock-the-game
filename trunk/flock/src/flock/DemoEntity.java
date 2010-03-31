package flock;

/// a demo entity displaying an image and moving to the right.
public class DemoEntity extends Entity
{
	public DemoEntity()
	{
		super("imageNotFound");
		_rect.x = 100;
		_rect.y = 100;
	}
	
	@Override
	public Object clone()
	{
		return super.clone();
	}

	@Override
	public void doUpdate(long msElapsed)
	{
		//_rect.x++;
		System.out.println("x " + _rect.x);
	}
}
