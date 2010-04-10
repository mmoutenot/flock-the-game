package flock;

public class LevelTop extends Level
{
	public LevelTop() throws Exception
	{
		super("top");
		_entities.add(new DoorEntity(600, 165, 10, "zero"));
		_entities.add(new WrenchEntity(100, 300));
		_entities.add(new FactoryEntity(150, 30, false, 15, 1000));
		_entities.add(new PlayerEntity(100, 470));
	}
}
