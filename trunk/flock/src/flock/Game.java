package flock;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

/**
 * Main game class. Contains the game and drawing loops (two separate threads),
 * as well as global access points for shared objects in the game (such as the
 * Config, the ImageManager, etc.)
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
				_player.tryJump();
				break;
			case TimeFreeze:
				setTimeFreeze(!_timeFreeze);
				break;
			case Pause:
				setPaused(!_paused, null);
				break;
			case PickUpItem:
			{
				ToolEntity pick = (ToolEntity)findNearestEntity(ToolEntity.class);
				if(pick == null) // too far; drop current item
					_player.dropTool();
				else
					_player.pickTool(pick);
				break;
			}
			case UseItem:
				_player.useTool();
				break;
			case Action:
			{
				ActionEntity pick = (ActionEntity)findNearestEntity(ActionEntity.class);
				if(pick != null)
					pick.action();
				break;
			}
			case Debug:
				if(_config.isDebugEnabled())
					_debugPressed = true;
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
	private CollisionManager _colman = null;
	private boolean _isRunning = false;
	private Thread _animator;
	private double _actualFps;
	private Level _currentLevel = null;
	private Tile[][] _tiles;
	private ArrayList<Entity> _entities;
	private PlayerEntity _player;
	private ArrayList<Entity> _killList;
	private ArrayList<Entity> _addList;
	private boolean _timeFreeze;
	private boolean _paused = false;
	private Overlay _pauseOverlay = null;
	/// If in debug mode, wait for a key press before doing an update.
	private boolean _debugPressed = false;
	
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
		_colman = new CollisionManager();
		
		if(!testing)
		{
			_canvas = new Canvas();
			_canvas.setPreferredSize(new Dimension(780,600));
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
	
	/// Starts playing the @p level.
	public void loadLevel(Level level)
	{
		_currentLevel = level;
		_tiles = level.tiles();
		_entities = level.entities();
		setTimeFreeze(true);
		setPaused(true, level.getOverlay(getImageForOverlay()));
		
		// Find the player and all ToolEntities.
		_player = null;
		_killList = new ArrayList<Entity>();
		_addList = new ArrayList<Entity>();
		
		for(Entity ent: _entities)
		{
			if(ent instanceof PlayerEntity)
				_player = (PlayerEntity)ent;
		}
		if(_player == null)
		{
			System.err.println("Level " + level.id() + " has no player!");
			System.exit(1);
		}
		_player.setFrozen(false);
	}
	
	/// Returns the current level.
	public Level currentLevel()
	{
		return _currentLevel;
	}
	
	/// Returns the nearest active entity of type @p type.
	private Entity findNearestEntity(Class<?> type)
	{
		double min = Double.POSITIVE_INFINITY;
		Entity pick = null;
		
		for(Entity ent: _entities)
		{
			if(!ent.isActive() || !type.isInstance(ent))
				continue;
			final double dist = _player.distance(ent);
			//System.out.println("Considering " + ent + " at dist " + dist);
			if(dist < min)
			{
				min = dist;
				pick = ent;
			}
		}
		
		//System.out.println("Nearest entity " + pick + " at dist " + min);
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
					
					// Render
					if(!_paused)
					{
						if(g == null || _currentLevel == null)
						{
							System.out.println("Animator thread skipping a beat -- no graphics or level yet.");
							continue;
						}
						draw(g);
					}
					else
					{
						if(g == null || _pauseOverlay == null)
						{
							System.out.println("Animator thread skipping a beat -- no graphics or pause overlay yet.");
							continue;
						}
						_pauseOverlay.draw(g);
					}
					
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
	
	/// Draws game onto graphics object @p g.
	@SuppressWarnings("unchecked")
	private void draw(Graphics2D g)
	{
		g.setBackground(new Color(230, 230, 230));
		g.clearRect(0, 0, getWidth(), getHeight());
		
		// The tiles.
		for(int r = 0; r < _currentLevel.rows(); r++)
			for(int c = 0; c < _currentLevel.cols(); c++)
				_tiles[r][c].draw(g, c * _config.tileWidth(), r * _config.tileHeight());
		
		// Grid, if in debug mode.
		if(_config.isDebugEnabled())
		{
			final int tW = _config.tileWidth(),
			          tH = _config.tileHeight(),
			          tR = _currentLevel.rows(),
			          tC = _currentLevel.cols();
			
			g.setColor(Color.CYAN);
			for(int r = 0; r < tR; r++)
				g.drawLine(0, (r + 1) * tH, tC * tW, (r + 1) * tH);
			for(int c = 0; c < tC; c++)
				g.drawLine((c + 1) * tW, 0, (c + 1) * tW, tR * tH);
		}
		
		// The entities.
		ArrayList<Entity> copy;
		synchronized(_entities)
		{
			copy = (ArrayList<Entity>) _entities.clone();
		}
		for(Entity ent: copy)
			ent.draw(g);
	}
	
	/// Takes a screenshot of the game.  Used by Overlays.
	public BufferedImage getImageForOverlay()
	{
		BufferedImage image = new BufferedImage(getWidth(),
				getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		draw(image.createGraphics());
		return image;
	}
	
	/// Main loop for the game/logic thread.
	public void gameLoop()
	{
		_keyman = new KeyManager(); // setPaused will add it
		requestFocus(); // grab the keyboard
		
		FPSManager fps = new FPSManager(config().updateFps());
		while(_isRunning)
		{
			fps.sleep();
			//System.out.println("gameLoop");
			
			// If in debug mode, pause everything until Debug is pressed.
			if(_config.isDebugEnabled())
				setPaused(!_debugPressed, null);
			
			// Update everything.
			// Note: it is the Entity's job to skip the update if paused.
			_debugPressed = false;
			List<Entity> ents = Collections.unmodifiableList(_entities);
			for(Entity ent: ents)
			{
				ent.update();
			}
			_colman.notifyCollisions();
			_colman.checkEnvironment();
			_keyman.update();
			addRemoveEntities();
		}
	}
	
	/// Applies to all entities that we want removed (including tools that have been used)
	public void addToKillList(Entity e)
	{
		_killList.add(e);
	}
	
	/// Called when we want to add an entity to the currently running game.
	public void addToAddList(Entity e)
	{
		_addList.add(e);
	}
	
	/// Removes kill-listed entities, adds add-listed entities.
	public void addRemoveEntities()
	{
		if(_killList.isEmpty() && _addList.isEmpty())
			return;
		
		synchronized(_entities)
		{
			for (Entity ent : _killList)
			{
				_entities.remove(ent);
				if (ent instanceof LemmingEntity)
				{
					((LemmingEntity)ent).kill();
				}
			}
			_killList.clear();
			
			for(Entity e: _addList)
			{
				_entities.add(0, e);
//				System.out.println("Added " + e);
			}
			_addList.clear();
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
	
	/// Returns the unique CollisionManager of this game.
	public CollisionManager collisionManager()
	{
		return _colman;
	}
	
	/// Returns the tiles of the current level.
	public Tile[][] getTiles()
	{
		return _tiles;
	}
	
	/// Returns all current entities.
	public ArrayList<Entity> getEntities()
	{
		return _entities;
	}
	
	/// Returns the PlayerEntity of the game.
	public PlayerEntity player()
	{
		return _player;
	}
	
	/// Sets time freeze on or off.
	public void setTimeFreeze(boolean freeze)
	{
		_timeFreeze = freeze;
		for(Entity ent : _entities)
		{
			if(!(ent instanceof PlayerEntity))
				ent.setFrozen(freeze);
		}
	}
	
	/**
	 * If @p paused is true, pauses the game, displaying overlay @p overlay.
	 * If @p paused is false, unpauses the game.
	 */
	public void setPaused(boolean paused, Overlay overlay)
	{	
		if(_paused == paused)
			return;
		
		_paused = paused;
		for(Entity ent : _entities)
		{
			ent.setPaused(paused);
		}
		
		if(paused)
		{
			if(overlay == null)
				overlay = new PauseOverlay(getImageForOverlay());
			_pauseOverlay = overlay;
			removeKeyListener(_keyman);
			addKeyListener(_pauseOverlay);
		}
		else
		{
			removeKeyListener(_pauseOverlay);
			_pauseOverlay = null;
			addKeyListener(_keyman);
		}
	}
	
	/// Loads a fresh copy of the level indicated by @p id.
	public void goToLevel(String id)
	{
		loadLevel(_levelman.level(id));
	}
	
	/// main app.
	public static void main(String[] args)
	{
		instance().run();
	}
}
