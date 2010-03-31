package flock;

import java.awt.*; // FIXME no *s
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Main game class. Contains the game and drawing loops (two separate threads),
 * as well as global access points for the configuration of the game and the
 * ImageManager of the game. 
 */
public class Game extends JPanel implements Runnable
{
	/// make Eclipse happy.
	private static final long serialVersionUID = 5332222116616788458L;
	
	/// singleton: the one and only running Game, accessible through instance().
	static private Game _theGame = null;
	private JFrame _window = null;
	private Config _config = null;
	private ImageManager _imageman = null;
	private LevelManager _levelman = null;
	private boolean _isRunning = false;
	private Thread _animator;
	private double _actualFps;
	private Level _currentLevel;
	private Tile[][] _tiles;
	private ArrayList<Entity> _entities;
	
	private Game()
	{
		setPreferredSize(new Dimension(800,600));
		_isRunning = false;
	}
	
	/**
	 *  "Second" constructor, to avoid infinite recursion when one of our
	 *  children calls Game.instance();
	 */
	private void init()
	{
		_config = new Config();
		_imageman = new ImageManager();
		_levelman = new LevelManager();
		
		// Main thread updates, animator thread paints;
		_animator = new Thread(this);
		_animator.start();
		
		_entities = new ArrayList<Entity>();
		_isRunning = true;
		
		_config.loadLevels();
		loadLevel(_levelman.defaultLevel());
	}
	
	private void loadLevel(Level level)
	{
		_currentLevel = level;
		_tiles = level.tiles();
		_entities = level.entities();
	}
	
	/// We store our parent window so that we can update its title.
	public void setWindow(JFrame window)
	{
		_window = window;
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
			System.out.println("animatorLoop");
			
			Graphics2D g = (Graphics2D) getGraphics();
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
			
			// TODO: double-buffering like Zapped.
			
			titleCounter++;
			if(titleCounter == config().paintFps())
			{
				titleCounter = 0;
				_actualFps = fps.actualFps();
				_window.setTitle(windowTitle());
			}	
		}
	}
	
	/// Main loop for the game/logic thread.
	public void gameLoop()
	{
		FPSManager fps = new FPSManager(config().updateFps());
		while(_isRunning)
		{
			fps.sleep();
			System.out.println("gameLoop");
			for(Entity ent: _entities)
			{
				ent.update();
				// TODO figure out what to do about collisions...
			}
		}
	}
	
	/// Returns an appropriate title for the game window.
	public String windowTitle()
	{
		return "Flock! (" + _actualFps + " FPS)"; // FIXME round
	}
	
	/// Returns the one and only instance of the running game.
	public static Game instance()
	{
		if(_theGame == null)
		{
			_theGame = new Game();
			_theGame.init();
		}
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
	
	/// main app.
	public static void main(String[] args)
	{
		Game game = instance();
		
		// A top-level window for our game.
		JFrame window = new JFrame(game.windowTitle());
		game.setWindow(window); // To update the title (this sucks).
		
		// Quit the game if the window is closed.
		window.addWindowListener(new WindowAdapter()
			{
		    	public void windowClosing(WindowEvent e)
		    	{
		    		System.exit(0);
		    	}
			});

		window.add(game);
		window.pack();
		window.setResizable(false);
		window.setVisible(true);
		game.run();
	}
}
