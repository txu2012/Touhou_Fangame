package TouhouFangame;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.io.File;
import javax.imageio.ImageIO;

public class SpriteStore {
	// Creates a single instance of class
	private static SpriteStore single = new SpriteStore();
	
	// Grabs instance of class
	public static SpriteStore get(){
		
		return single;
		
	}
	
	private HashMap sprites = new HashMap();
	
	public Sprite getSprite(String filename){
		
		if(sprites.get(filename) != null){
			return (Sprite) sprites.get(filename);
		}
		
		BufferedImage sourceImage = null;
		
		try{
			
			URL file = this.getClass().getClassLoader().getResource(filename);
			//sourceImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(filename));
			if(file == null){
				
				fail("Can't find ref: " + filename);
				
			}
			
			sourceImage = ImageIO.read(file);
		}
		catch(Exception e){
			
			fail("Failed to load: " + filename);
			
		}
		
		// Creates accelerated image to the right size
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
		
		// Draws image into accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);
		
		Sprite sprite = new Sprite(image);
		sprites.put(filename, sprite);
		
		return sprite;
		
	}
	
	// Creates a fail message and exits game
	private void fail(String message){
		
		System.err.println(message);
		System.exit(0);
		
	}
}
