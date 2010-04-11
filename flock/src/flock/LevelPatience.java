package flock;

public class LevelPatience extends Level
{
	public LevelPatience() throws Exception
	{
		super("patience");
		
		_entities.add(new DoorEntity(700, 520, 2, "zero"));
		_entities.add(new DoorEntity(700, 100, 2, "zero"));
		_entities.add(new FactoryEntity(300, 0, false, 50, 1000));
		
		_entities.add(new PlayerEntity(100, 50));
	}
}
