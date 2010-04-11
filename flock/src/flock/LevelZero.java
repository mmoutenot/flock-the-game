package flock;

import java.util.concurrent.Callable;

/**
 * The tutorial level.
 * 
 * NOTE: This whole level is a giant HACK. It requires a lot of changes
 * to the currently running level, which is doable but not pretty.
 * 
 * NOTE: If keys are changed in FlockKeyListener, the tutorial texts
 * should be modified too.
 */
public class LevelZero extends Level
{
	/// Custom TextEntity that calls a functor on collisions with the player.
	private class TutorialText extends TextEntity
	{
		private Callable<Void> _func;
		
		public TutorialText(double x, double y, double w, double h, String[] l, Callable<Void> func)
		{
			super(x, y, w, h, l);
			_frozen = false;
			_active = true;
			_collide = true;
			_func = func;
		}
		
		@Override
		protected void collided(Entity other)
		{
			if(other instanceof PlayerEntity)
			{
				_collide = false; // first time only
				try
				{
					_func.call();
				}
				catch (Exception e)
				{
					System.err.println("LevelZero.TutorialText functor failed!");
					e.printStackTrace();
				}
			}
		}
	}
	
	/// Custom wrench that builds doors.
	private class DoorBuildingWrench extends WrenchEntity
	{
		public DoorBuildingWrench(double x, double y)
		{
			super(x, y);
		}
		
		@Override
		public void pickUp()
		{
			super.pickUp();
			
			String[] msg3 =
			{
					"This is a very special wrench you just picked up.",
					"It can build doors for the lemmings to go through.",
					"Go to the lemming platform and try using it (X key)."
			};
			_text3.setLines(msg3);
		}
		
		@Override
		public void use()
		{
			Game.instance().getEntities().add(0, new DoorEntity(_x, _y, 1, "test"));
			
			String[] msg2 =
			{
					"Remember: SPACE to perform an action.",
					"Z to freeze / unfreeze time.",
					"DOWN to pick up or drop a tool.",
					"X to use a tool at the current location."
			};
			_text2.setLines(msg2);
			
			String[] msg3 =
			{
					"Unfreeze time again and let the lemming through",
					"the door. Then go through the door",
					"yourself (SPACE) to get to the next level!"
			};
			_text3.setLines(msg3);
		}
	}
	
	private TutorialText _text1, _text2, _text3;
	
	public LevelZero() throws Exception
	{
		super("zero");
		
		
		// Step 1. Jump up here and press the button.
		String[] msg1 =
		{
				"First time playing?",
				"Use the arrow keys to move the character.",
				"Jump up here and I'll teach you how to play."
		};
		_text1 = new TutorialText(40, 350, 300, 60, msg1,
				new Callable<Void>()
				{
					@Override
					public Void call()
					{
						String[] msg =
						{
								"Great job!",
								"A button appeared next to you.",
								"Go activate it by pressing SPACE."
						};
						_text1.setLines(msg);
						
						ButtonEntity button = new ButtonEntity(40, 410)
						{
							@Override
							protected void stateChanged(boolean state)
							{
								String[] msg =
								{
										"Awesome!",
										"Remember that SPACE performs an action",
										"on an object in the game."
								};
								_text1.setLines(msg);
								_text2.setVisible(true);
								Game.instance().getEntities().add(0, new LemmingEntity(500, 350));
							}
						};
						Game game = Game.instance();
						game.getEntities().add(0, button);
						
						return null;
					}
				});
		
		// Step 2. Lemmings and time freeze.
		String[] msg2 =
		{
				"Now, lemmings. They are good-natured but stupid.",
				"You want them to go safely through the door,",
				"without falling onto spikes or getting killed by",
				"antilemmings. (Jump up here to continue.)"
		};
		_text2 = new TutorialText(420, 250, 320, 75, msg2,
				new Callable<Void>()
				{
					@Override
					public Void call()
					{
						String[] msg =
						{
								"To keep the lemmings from doing stupid things,",
								"you can freeze and unfreeze time with the Z key.",
								"Try it!",
								"Then jump up to the next platform."
						};
						_text2.setLines(msg);
						_text3.setVisible(true);
						return null;
					}
				});
		_text2.setVisible(false);
		
		// Step 3. Tools and using them.
		String[] msg3 =
		{
				""
		};
		_text3 = new TutorialText(40, 150, 300, 75, msg3,
				new Callable<Void>()
				{
					@Override
					public Void call()
					{
						String[] msg1 =
						{
								"What is the deal with the terminal?",
								"If you know the password to a level,",
								"you can type it in to go there directly."
						};
						_text1.setLines(msg1);
						
						String[] msg2 =
						{
								"Remember: SPACE to perform an action.",
								"Z to freeze / unfreeze time."
						};
						_text2.setLines(msg2);
						
						String[] msg3 =
						{
								"See this wrench? It's one of the tools in the game.",
								"You can pick up a tool with the DOWN arrow.",
								"When you pick up a tool, it moves with you.",
								"Then you can use it with the X key."
						};
						_text3.setLines(msg3);
						Game.instance().getEntities().add(0, new DoorBuildingWrench(100, 230));
						
						return null;
					}
				});
		_text3.setVisible(false);
		
		_entities.add(_text1);
		_entities.add(_text2);
		_entities.add(_text3);
		_entities.add(new TerminalEntity(600, 500));
		
		PlayerEntity player = new PlayerEntity(550, 500);
		player.setCollides(true); // HACK: in this level, need to react to collisions
		_entities.add(player); // player last
	}
}
