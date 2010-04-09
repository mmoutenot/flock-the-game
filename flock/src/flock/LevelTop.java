package flock;

public class LevelTop extends Level
{
	public LevelTop() throws Exception
	{
		super("top");
		_entities.add(new DoorEntity(500, 100, 0, "zero"));
		_entities.add(new WrenchEntity(100, 100));
	}
}
