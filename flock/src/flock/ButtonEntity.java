package flock;

import java.awt.Image;

/**
 * An abstract button.
 * Subclass an implement stateChanged().
 */
abstract public class ButtonEntity extends ActionEntity
{
	private boolean _pressed = false;
	private Image _off, _on;
	
	public ButtonEntity(double x, double y)
	{
		super("redbutton", x, y);
		_off = _image;
		_on = Game.instance().imageManager().getImage("bluebutton");
	}

	@Override
	public void action()
	{
		_pressed = !_pressed;
		_image = _pressed ? _on : _off;
		stateChanged(_pressed);
	}

	@Override
	protected void doUpdate(long msElapsed)
	{
	}
	
	abstract protected void stateChanged(boolean state);
}
