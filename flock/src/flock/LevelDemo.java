package flock;

public class LevelDemo extends Level
{
	public LevelDemo() throws Exception
	{
		super("test");
		_entities.add(new DemoEntity());
		_entities.add(new LemmingEntity(200, 200));
		_entities.add(new PlayerEntity(300, 300));
		_entities.add(new WrenchEntity(400, 400));
	}
}
