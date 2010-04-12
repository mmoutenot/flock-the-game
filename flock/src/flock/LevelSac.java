package flock;

public class LevelSac extends Level
{
	public LevelSac() throws Exception
	{
		super("sac");
		
		_entities.add(new WrenchEntity(300, 200));
		_entities.add(new DoorEntity(40, 524, 2, "patience"));
		_entities.add(new FactoryEntity(100, 0, true, 5, 1000));
		FactoryEntity f = new FactoryEntity(150, 150, false, 5, 1000);
		f.setBothWays(false);
		_entities.add(f);
		_entities.add(new FactoryEntity(50, 400, true, 5, 1000));
		_entities.add(new BlockEntity(250, 190));
		
		_entities.add(new PlayerEntity(300, 150));
	}
}
