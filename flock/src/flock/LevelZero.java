package flock;

public class LevelZero extends Level
{
	public LevelZero() throws Exception
	{
		super("zero");
		_entities.add(new TerminalEntity(600, 400));
		_entities.add(new PlayerEntity(100, 100));
		_entities.add(new DoorEntity(500, 524, 0, 1));
	}
}
