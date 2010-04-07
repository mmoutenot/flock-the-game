package flock;

public class LevelDemo extends Level
{
	public LevelDemo() throws Exception
	{
		super("test");
		_entities.add(new DemoEntity());
		_entities.add(new LemmingEntity(200, 240));
		_entities.add(new AntiLemmingEntity(300, 240));
		//_entities.add(new LemmingEntity(150, 100));
		_entities.add(new LemmingEntity(130, 100));
		//_entities.add(new AntiLemmingEntity(50, 100));
		_entities.add(new WrenchEntity(250, 50));
		_entities.add(new DoorEntity(100, 224));
		
		// Player should be added last, so it draws on top of everything else.
		_entities.add(new PlayerEntity(100, 100));
		//_entities.get(_entities.size() - 1).setDebug(true);
	}
}
