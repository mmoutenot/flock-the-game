package flocktest;

import java.util.ArrayList;

import flock.Entity;
import flock.Game;
import flock.LevelDemo;
import flock.Tile;

/// Unit test for the Level class.
public class LevelTest
{
	public static void main(String[] args)
	{
		try
		{
			// Prevent an actual instance of the Game from starting.
			Game.testInstance();
			
			LevelDemo test = new LevelDemo();
			
			/*
			 * DEEP COPY DISABLED!
			 * 
			// Check that deep copy works like we want it to.
			{
				Tile[][] tiles = test.tiles();
				tiles[0][0].setGravity(tiles[0][0].gravity() + 2);
				Tile[][] copy = test.tiles();
				if(tiles[0][0].gravity() == copy[0][0].gravity()) // change was propagated	
				{
					System.err.println("Deep-copy for tiles FAILED.");
					System.exit(1);
				}
				System.err.println("Deep-copy for tiles OK.");
			}
			{
				ArrayList<Entity> entities = test.entities();
				if(entities.size() == 0)
				{
					System.err.println("Need at least one entity...");
					System.exit(3);
				}
				entities.get(0).setX(entities.get(0).rect().x + 2);
				ArrayList<Entity> copy = test.entities();
				if(entities.get(0).rect().x == copy.get(0).rect().x) // change propagated
				{
					System.err.println("Deep-copy for entities FAILED.");
					System.exit(2);
				}
				System.err.println("Deep-copy for entities OK.");
			}
			*/
			
			// TODO incomplete
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(123);
		}
	}
}
