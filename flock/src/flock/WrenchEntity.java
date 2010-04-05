package flock;

/**
 * An entity that builds bridges.
 */
public class WrenchEntity extends ToolEntity
{
	public WrenchEntity(double x, double y)
	{
		super("wrench", x, y);
	}

	@Override
	public void doUpdate(long msElapsed)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void use()
	{
		// TODO Auto-generated method stub
		System.out.println("Wrench used at " + _x + ", " + _y);
	}
}
