package flock;

public class LevelDemo extends Level
{
	public LevelDemo() throws Exception
	{
		super("test");
		_entities.add(new DemoEntity());
	}
}
