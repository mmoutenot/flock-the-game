package flock;

public class TerminalEntity extends ActionEntity
{
	public TerminalEntity(double x, double y)
	{
		super("terminal", x, y);
	}

	@Override
	public void doUpdate(long msElapsed)
	{
	}
	
	@Override
	public void action()
	{
		System.out.println("boom!");
	}
}
