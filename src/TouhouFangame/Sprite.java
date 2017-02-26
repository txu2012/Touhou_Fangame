package TouhouFangame;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
	// Creates an image object for the class
	private Image image;
	
	// Create sprite based on the image
	public Sprite(Image image){
		this.image = image;
	}
	
	// Returns the width of image
	public int getWidth(){
		
		return image.getWidth(null);
		
	}
	
	// Returns height of image
	public int getHeight(){
		
		return image.getHeight(null);
		
	}
	
	/**
	 * Draws the sprite onto the graphics
	 * 
	 * @param g The graphics to draw sprite on
	 * @param x The X location
	 * @param y The Y location
	 */
	public void draw(Graphics g, int x, int y){
		
		g.drawImage(image,x,y,null);
		
	}
}
