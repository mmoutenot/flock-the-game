package flock;

import java.awt.image.BufferedImage;
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
		loadImage("imageNotFound", null);
	}
	
	/// loads an image from the Game class resource.
	private void loadImage(String id, BufferedImage fallback)
	{
		BufferedImage img;
		try
		{
			System.out.println("img name " + id);
			URL url = Game.instance().getClass().getClassLoader().getResource("flockimg/" + id + ".png");
			// TODO document "flockimg/" and figure out where best to put images.
			System.out.println("URL = " + url);
			img = ImageIO.read(url);
		}
		catch (Exception e)
		{
			System.err.println("Image loading error: " + id);
			e.printStackTrace();
			img = fallback;
		}
		_images.put(id, img);
	}
	
	/// returns an image (loads it if not loaded yet).
	public BufferedImage getImage(String id)
	{
		if(!_images.containsKey(id))
			loadImage(id, _images.get("imageNotFound"));
		return _images.get(id);
	}
}
