package flock;

public class LevelCatch extends Level
{
	/// A custom PlayerEntity that kills antilemmings upon collision.
	private class CustomPlayer extends PlayerEntity
	{
		public CustomPlayer(double x, double y)
		{
			super(x, y);
			_collide = true;
		}
		
		@Override
		protected void collided(Entity other)
		{
			if(other instanceof AntiLemmingEntity)
				Game.instance().addToKillList(other);
		}
	}
	public LevelCatch() throws Exception
	{
		super("catch");
		
		_entities.add(new DoorEntity(700, 520, 15, "patience"));
		_entities.add(new FactoryEntity(350, 0, true, 100, 1500));
		
		// "Explanatory" text:
		String[] msg = { "You have special powers!" };
		_entities.add(new TextEntity(275, 220, 200, 30, msg));
		
		// A bunch of lemmings:
		final int[][] pos =
		{
				{ 35, 210 },
				{ 65, 210 },
				{ 95, 210 },
				{ 655, 210 },
				{ 685, 210 },
				{ 715, 210 },
				{ 35, 325 },
				{ 65, 325 },
				{ 95, 325 },
				{ 655, 325 },
				{ 685, 325 },
				{ 715, 325 },
				{ 220, 265 },
				{ 250, 265 },
				{ 280, 265 },
				{ 440, 265 },
				{ 470, 265 },
				{ 500, 265 }
		};
		for(int i = 0; i < pos.length; i++)
			_entities.add(new LemmingEntity(pos[i][0], pos[i][1]));
		
		_entities.add(new CustomPlayer(340, 100));
	}
}

