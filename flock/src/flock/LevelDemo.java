package flock;

public class LevelDemo extends Level
{
	public LevelDemo() throws Exception
	{
		super("test");
		_entities.add(new DemoEntity());
		_entities.add(new LemmingEntity(200, 200));
		_entities.add(new PlayerEntity(100, 100));
		_entities.add(new WrenchEntity(250, 50));
	}
}
