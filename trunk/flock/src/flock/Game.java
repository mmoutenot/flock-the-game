package flock;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Main game class. Contains the game and drawing loops (two separate threads),
 * as well as global access points for the configuration of the game and the
 * ImageManager of the game.
 */
public class Game extends JFrame implements Runnable
{
	/**
	 * Handles all key presses.
	 * Implemented as an inner class of Game since it alters its data structures
	 * directly.
	 * 
	 * Uses some hacks to get around stupid key repeats...
	 */
	private class KeyManager extends FlockKeyListener
	{
		/**
		 * Milliseconds to delay stopping after we get a keyReleased.
		 * This is necessary because key autorepeat sends repeated
		 * keyPressed and keyReleased events, and we don't want to
		 * stop the character in the middle of motion.
		 */
		final long STOP_DELAY = 10;
		private boolean _stopScheduled = false;
		private long _stopTime = 0;
		
		/// Call this from the game loop.
		public void update()
		{
			if(_stopScheduled && System.currentTimeMillis() - _stopTime >= STOP_DELAY)
			{
				_player.setVelX(0);
				_stopScheduled = false;
			}
		}
				
		@Override
		public void keyPressed(KeyEvent e)
		{
			startMovePlayer(getKey(e));
			// NOTE: eventually we'll want to handle the pause menu here as well,
			// depending on whether the game is paused.
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
			stopMovePlayer(getKey(e));
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
		}
		
		private void startMovePlayer(Key key)
		{
			if(_player == null)
			{
				System.err.println("No player in KeyManager.startMovePlayer()");
				return;
			}
			
			switch(key)
			{
			case Left:
				_player.setVelX(-config().defaultPlayerMotionSpeed());
				_stopScheduled = false;
				break;
			case Right:
				_player.setVelX(config().defaultPlayerMotionSpeed());
				_stopScheduled = false;
				break;
			case Jump:
				// FIXME jumping should be possible only when we're on top of solid ground.
				_player.setVelY(-config().defaultPlayerJumpSpeed());
				break;
			case TimeFreeze:
				setTimeFreeze(!_timeFreeze);
				break;
			case PickUpItem:
				ToolEntity pick = findNearestTool();
				if(pick == null) // too far; drop current item
					_player.dropTool();
				else
					_player.pickTool(pick);
				break;
			case UseItem:
				_player.useTool();
				break;
			}
		}
		
		private void stopMovePlayer(Key key)
		{
			if(_player == null)
			{
				System.err.println("No player in KeyManager.stopMovePlayer()");
				return;
			}
			
			switch(key)
			{
			case Left:
			case Right:
//				System.out.println("stopped motion");
				_stopScheduled = true;
				_stopTime = System.currentTimeMillis();
				break;
			}
		}
		
		
	}
	
	/// make Eclipse happy.
	private static final long serialVersionUID = 5332222116616788458L;
	
	/// singleton: the one and only running Game, accessible through instance().
	static private Game _theGame = null;
	private Canvas _canvas = null;
	private Config _config = null;
	private ImageManager _imageman = null;
	private LevelManager _levelman = null;
	private KeyManager _keyman = null;
	private boolean _isRunning = false;
	private Thread _animator;
	private double _actualFps;
	private Level _currentLevel;
	private Tile[][] _tiles;
	private ArrayList<Entity> _entities;
	private ArrayList<ToolEntity> _toolEntities;
	private PlayerEntity _player;
	private boolean _timeFreeze;
	
	private Game()
	{
		_isRunning = false;
	}
	
	/**
	 *  "Second" constructor, to avoid infinite recursion when one of our
	 *  children calls Game.instance();
	 */
	private void init(boolean testing)
	{
		_config = new Config();
		_imageman = new ImageManager();
		_levelman = new LevelManager();
		
		if(!testing)
		{
			_canvas = new Canvas();
			_canvas.setPreferredSize(new Dimension(800,600));
			add(_canvas);
			pack();
			setResizable(false);

			setVisible(true);
			_canvas.createBufferStrategy(2);
			
			// Quit the game if the window is closed.
			addWindowListener(new WindowAdapter()
				{
			    	public void windowClosing(WindowEvent e)
			    	{
			    		System.exit(0);
			    	}
				});
			
			// Main thread updates, animator thread paints;
			_animator = new Thread(this);
			_animator.start();
			
			_config.loadLevels();
			_isRunning = true;
			loadLevel(_levelman.defaultLevel());
		}
	}
	
	private void loadLevel(Level level)
	{
		_currentLevel = level;
		_tiles = level.tiles();
		_entities = level.entities();
		setTimeFreeze(true);
		
		// Find the player and all ToolEntities.
		_player = null;
		_toolEntities = new ArrayList<ToolEntity>();
		for(Entity ent: _entities)
		{
			if(ent instanceof PlayerEntity)
				_player = (PlayerEntity)ent;
			else if(ent instanceof ToolEntity)
				_toolEntities.add((ToolEntity)ent);
		}
		if(_player == null)
		{
			System.err.println("Level " + level.id() + " has no player!");
			System.exit(1);
		}
	}
	
	private ToolEntity findNearestTool()
	{
		double min = Double.POSITIVE_INFINITY;
		ToolEntity pick = null;
		
		for(ToolEntity ent: _toolEntities)
		{
			if(ent == _player.tool())
				continue;
			final double dist = _player.distance(ent);
			System.out.println("Considering " + ent + " at dist " + dist);
			if(dist < min)
			{
				min = dist;
				pick = ent;
			}
		}
		
		System.out.println("Nearest entity " + pick + " at dist " + min);
		if(min < _config.defaultPickToolDistance())
			return pick;
		else
			return null;
	}
	
	/// Entry point into the game. Returns when game is finished.
	public void run()
	{
		if(Thread.currentThread() == _animator)
		{
			animatorLoop();
		}
		else
		{
			gameLoop();
		}
	}
	
	/// Main loop for the drawing/animator thread.
	public void animatorLoop()
	{
		FPSManager fps = new FPSManager(config().paintFps());
		int titleCounter = 0;
		while(true) // draw even if not _isRunning
		{
			fps.sleep();
			//System.out.println("animatorLoop");
			
			// Code from BufferStrategy docs:
			BufferStrategy strategy = _canvas.getBufferStrategy();
			do
			{
				// The following loop ensures that the contents of the drawing buffer
				// are consistent in case the underlying surface was recreated
				do
				{
					// Get a new graphics context every time through the loop
					// to make sure the strategy is validated
					Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
					//Graphics2D g = (Graphics2D) getContentPane().getGraphics();
					
					// Render
					if(g == null)
					{
						System.out.println("Animator thread skipping a beat -- no graphics yet.");
						continue;
					}
					g.clearRect(0, 0, getWidth(), getHeight());
					for(int r = 0; r < _currentLevel.rows(); r++)
						for(int c = 0; c < _currentLevel.cols(); c++)
							_tiles[r][c].draw(g, c * _config.tileWidth(), r * _config.tileHeight());
					for(Entity ent: _entities)
						ent.draw(g);
					
					// Dispose the graphics
					g.dispose();
					
					// Repeat the rendering if the drawing buffer contents 
					// were restored
				} while (strategy.contentsRestored());
			
				// Display the buffer
				strategy.show();
				
				// Repeat the rendering if the drawing buffer was lost
			} while(strategy.contentsLost());

			titleCounter++;
			if(titleCounter == config().paintFps())
			{
				titleCounter = 0;
				_actualFps = fps.actualFps();
				setTitle(windowTitle());
			}	
		}
	}
	
	/// Main loop for the game/logic thread.
	public void gameLoop()
	{
		_keyman = new KeyManager();
		addKeyListener(_keyman);
		requestFocus(); // grab the keyboard
		
		FPSManager fps = new FPSManager(config().updateFps());
		while(_isRunning)
		{
			fps.sleep();
			//System.out.println("gameLoop");
			for(Entity ent: _entities)
			{
				ent.update();
				// TODO figure out what to do about collisions...
			}
			
			_keyman.update();
		}
	}
	
	/// Returns an appropriate title for the game window.
	public String windowTitle()
	{
		final int fps = (int)(_actualFps * 100);
		return "Flock! (" + fps / 100.0 + " FPS)";
	}
	
	/// Returns the one and only instance of the running game.
	public static Game instance()
	{
		if(_theGame == null)
		{
			_theGame = new Game();
			_theGame.init(false);
		}
		return _theGame;
	}
	
	/**
	 * Creates a "test" instance of the game -- i.e. one that doesn't
	 * actually load the levels and start running. Useful for testing.
	 * Must be called before a "real" instance of the game is created.
	 */
	public static Game testInstance()
	{
		if(_theGame != null)
		{
			System.err.println("Game instance already created when testInstance() was called...");
			System.exit(255);
		}
		_theGame = new Game();
		_theGame.init(true);
		return _theGame;
	}
	
	/// Returns the unique Config object of this game.
	public Config config()
	{
		return _config;
	}
	
	/// Returns the unique ImageManager of this game.
	public ImageManager imageManager()
	{
		return _imageman;
	}
	
	/// Returns the unique LevelManager of this game.
	public LevelManager levelManager()
	{
		return _levelman;
	}
	
	public void setTimeFreeze(boolean freeze)
	{
		_timeFreeze = freeze;
		for(Entity ent : _entities)
		{
			if(!(ent instanceof PlayerEntity))
				ent.setFrozen(freeze);
		}
	}
	
	/// main app.
	public static void main(String[] args)
	{
		instance().run();
	}
}
