package flock;

/**
 * Aids in maintaining a consistent FPS for a thread.
 * Also computes statistics about "actual" FPS.
 * 
 * Call sleep() in your loop to make sure it runs at the desired FPS. 
 */
// FIXME it's called 'Fps' not 'FPS' everywhere else.
public class FPSManager
{
	/// the desired FPS.
	int _fps;
	/// how many milliseconds to wait between frames.
	int _frameMs;
	/// what was the time when last sleep() ended.
	long _lastTime;
	/// counts to 5 seconds, then wraps.
	long _fiveSecCounter = 0;
	/// counts frames; wraps when _fiveSecCounter does.
	int _fiveSecFrames = 0;
	/// the actual FPS in the last 5 seconds.
	double _actualFps = 0;
	
	public FPSManager(int fps)
	{
		_fps = fps;
		_frameMs = 1000 / fps;
		_lastTime = System.currentTimeMillis();
	}
	
	public void sleep()
	{
		// If we're going too fast and need to sleep, do so.
		long delay = Math.max(0, _lastTime + _frameMs - System.currentTimeMillis());
		try
		{
			Thread.sleep(delay);
		}
		catch (InterruptedException e)
		{
			System.err.println("Error sleeping in thread " + Thread.currentThread());
			e.printStackTrace();
		}
		
		_fiveSecFrames++;
		_lastTime = System.currentTimeMillis();
		
		if(_lastTime - _fiveSecCounter > 5000)
		{
			_actualFps = _fiveSecFrames * 1000.0 / (_lastTime - _fiveSecCounter);
			_fiveSecCounter = _lastTime;
			_fiveSecFrames = 0;
		}
	}
	
	public double actualFps()
	{
		return _actualFps;
	}
}
