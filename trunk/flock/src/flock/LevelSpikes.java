package flock;

public class LevelSpikes extends Level
{
	public LevelSpikes() throws Exception
	{
		super("spikes");
		
		_entities.add(new WrenchEntity(660, 50));
		_entities.add(new DoorEntity(700, 520, 10, "zero"));
		_entities.add(new FactoryEntity(150, 0, false, 5, 1000));
		_entities.add(new FactoryEntity(560, 0, false, 5, 1000));
		_entities.add(new BlockEntity(340, 100));
		_entities.add(new BlockEntity(420, 100));
		
		_entities.add(new PlayerEntity(100, 50));
	}

}
