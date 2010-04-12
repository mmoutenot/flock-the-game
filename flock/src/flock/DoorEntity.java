package flock;

import java.awt.Color;
import java.awt.Graphics2D;

/// A door through which LemmingEntities can go through.
public class DoorEntity extends ActionEntity
{
	private int _lemmingsThrough;
	private int _lemmingsRequired;
	private String _nextLevel; //having this in the door class allows us to have multiple doors in the same room
	
	public DoorEntity(double x, double y, int lemmings, String next) 
	{
		super("door", x, y);
		_lemmingsThrough = 0;
		_lemmingsRequired = lemmings;
		_nextLevel = next;
		
		_collide = true;
		//doors don't move
		_velX = 0;
		_velY = 0;
		_accelX = 0;
		_accelY = 0;
	}

	@Override
	public void doUpdate(long msElapsed) 
	{
		;
	}
	
	@Override
	protected void collided(Entity other)
	{
		if(other instanceof LemmingEntity &&
		   !(other instanceof AntiLemmingEntity)) // HACK
		{
			Game.instance().addToKillList((LemmingEntity)other);
			_lemmingsThrough++;
			Game.instance().scoreUp();
			System.out.println("Number of lemmings in door: " + _lemmingsThrough);
		}
	}
	
	/// Draw some numbers telling the player how many lemmings he has to get in.
	@Override
	public void draw(Graphics2D g)
	{
		super.draw(g);
		
		final String str = _lemmingsThrough + "/" + _lemmingsRequired;
		if(_lemmingsThrough >= _lemmingsRequired)
			g.setColor(new Color(0.0f, 0.8f, 0.0f));
		else
			g.setColor(Color.RED);
		g.drawString(str, (int)_x, (int)_y - 2);
	}
	
	@Override
	public void action() 
	{
		if (_lemmingsThrough >= _lemmingsRequired)
		{
			Game.instance().goToLevel(_nextLevel);
		}
	}
}
