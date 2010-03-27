package flock;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Loads and stores all images used by the game.
 * The only instance is created in Game.
 * 
 * Eventually this can be extended to handle themes / skins.
 */
public class ImageManager
{
	private HashMap<String, BufferedImage> _images;
	
	ImageManager()
	{
		_images = new HashMap<String, BufferedImage>();
		loadImage("imageNotFound.png", null);
	}
	
	/// loads an image from the Game class resource.
	private void loadImage(String name, BufferedImage fallback)
	{
		BufferedImage img;
		try
		{
			System.out.println("img name " + name);
			URL url = Game.instance().getClass().getClassLoader().getResource("flockimg/" + name);
			// TODO document "flockimg/" and figure out where best to put images.
			System.out.println("URL = " + url);
			img = ImageIO.read(url);
		}
		catch (IOException e)
		{
			System.err.println("Image loading error: " + name);
			e.printStackTrace();
			img = fallback;
		}
		_images.put(name, img);
	}
	
	/// returns an image (loads it if not loaded yet).
	public BufferedImage getImage(String name)
	{
		if(!_images.containsKey(name))
			loadImage(name, _images.get("imageNotFound.png"));
		return _images.get(name);
	}
}
