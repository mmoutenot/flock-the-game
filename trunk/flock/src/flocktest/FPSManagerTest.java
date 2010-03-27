package flocktest;

import flock.FPSManager;

/**
 * A simple unit test for FPSManager.
 * Starts two threads and verifies that they run at the desired FPS.
 */
public class FPSManagerTest
{
	private class Sleeper implements Runnable
	{
		private int _fps = 0;
		private int _runs = 0;
		private FPSManager _fpsm;
		
		public Sleeper(int fps)
		{
			_fps = fps;
		}
		
		public void run()
		{
			_fpsm = new FPSManager(_fps);
			while(true)
			{
				_fpsm.sleep();
				_runs++;
			}
		}
		
		public int runs()
		{
			return _runs;
		}
		
		public double actualFps()
		{
			return _fpsm.actualFps();
		}
	}
	
	/*public FPSManagerTest()
	{
	}*/
	
	private void checkSleeper(Sleeper sl, int fps, int sec)
	{
		final int eps = 2;
		final double feps = 0.5;

		System.out.println("Sleeper ran " + sec + " seconds " + " at " + fps + " fps.");
		System.out.println("Expected frames " + fps * sec + "; actual frames " + sl.runs());
		if(Math.abs(sl.runs() - fps * sec) > eps)
			System.exit(1);
		System.out.println("Sleeper says it ran at an average of " + sl.actualFps() + " fps.");
		if(Math.abs(sl.actualFps() - fps) > feps)
			System.exit(1);
		System.out.println();
	}
	
	public void run()
	{
		final int afps = 20, bfps = 30, sec = 7;
		Sleeper a = new FPSManagerTest.Sleeper(afps), b = new Sleeper(bfps);
		Thread ta = new Thread(a), tb = new Thread(b);
		ta.start();
		tb.start();
		
		// Sleep for some seconds.
		try
		{
			System.out.println("Sleeping " + sec + " seconds.");
			Thread.sleep(sec * 1000);
		}
		catch (InterruptedException e)
		{
			System.err.println("Error sleeping in main test thread.");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Check that the FPSManager did what it was supposed to.
		checkSleeper(a, afps, sec);
		checkSleeper(b, bfps, sec);
		System.out.println("All good.");
		System.exit(0);
	}
	
	public static void main(String[] args)
	{
		// TODO read about whatever unit testing system Java has.
		FPSManagerTest test = new FPSManagerTest();
		test.run();
	}
}
