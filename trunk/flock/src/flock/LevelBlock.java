package flock;

public class LevelBlock extends Level
{
	public LevelBlock() throws Exception
	{
		super("block");
		
		_entities.add(new WrenchEntity(660, 50));
		_entities.add(new DoorEntity(700, 520, 10, "buttons"));
		_entities.add(new FactoryEntity(100, 0, false, 10, 1000));
		_entities.add(new FactoryEntity(250, 450, true, 5, 1000));
		_entities.add(new BlockEntity(50, 220));
		
		_entities.add(new PlayerEntity(100, 100));
	}
}
