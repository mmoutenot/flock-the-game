package flock;

public class LevelButtons extends Level
{
	private class CustomButton extends ButtonEntity
	{
		public CustomButton(double x, double y)
		{
			super(x, y);
		}

		@Override
		protected void stateChanged(boolean state)
		{
			final int[][] touch =
			{
					{  9, 12 },
					{ 10, 11 },
					{ 10, 12 },
					{ 10, 13 },
					{ 13, 11 },
					{ 13, 12 },
					{ 13, 13 }
			};
			Tile[][] tiles = Game.instance().getTiles();
			
			for(int i = 0; i < touch.length; i++)
			{
				Tile t = state ? new WallTile() : new AirTile();
				tiles[ touch[i][0] ][ touch[i][1] ] = t;
			}
		}
	}
	
	public LevelButtons() throws Exception
	{
		super("buttons");
		
		_entities.add(new DoorEntity(700, 520, 2, "catch"));
		_entities.add(new FactoryEntity(100, 100, false, 2, 1500));
		_entities.add(new FactoryEntity(530, 100, true, 2, 1000));
		_entities.add(new CustomButton(300, 500));
		
		_entities.add(new PlayerEntity(340, 100));
	}
}

