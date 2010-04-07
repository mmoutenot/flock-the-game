package flock;

public class LevelZero extends Level
{
	public LevelZero() throws Exception
	{
		super("zero");
		_entities.add(new TerminalEntity(600, 400));
		_entities.add(new PlayerEntity(100, 100));
	}
}
