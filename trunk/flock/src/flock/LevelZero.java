package flock;

public class LevelZero extends Level
{
	public LevelZero() throws Exception
	{
		super("zero");
		_entities.add(new TerminalEntity(600, 500));
		_entities.add(new DoorEntity(500, 524, 0, "test"));
		
		_entities.add(new PlayerEntity(550, 500)); // player last
	}
}
