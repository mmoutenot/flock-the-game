package flock;

public class LevelFinish extends Level
{
	public LevelFinish() throws Exception
	{
		super("finish");
		String[] msg = {"Congratulations! You made it to the end!", 
						"You've saved " + Game.instance().score() + " lemmings!",
						"Go through the door to save more lemmings!"};
		_entities.add(new TextEntity(275, 220, 255, 55, msg));
		_entities.add(new DoorEntity(370, 524, 0, "zero"));
		_entities.add(new PlayerEntity(370, 500));
		
	}
}
