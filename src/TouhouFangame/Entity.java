package TouhouFangame;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
	protected double x;
	protected double y;
	protected double dy;
	protected double dx;
	protected Sprite sprite;
	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();
	private EnemyShotPatterns pattern = new EnemyShotPatterns();
	private boolean playerShot = false;
	private boolean enemy = false;
	private boolean player = false;
	private boolean enemyShot = false;
	private boolean playerHitbox = false;
	private int enemyCrashDmg = 100;
	
	public Entity(String ref, int x, int y, boolean fired, boolean enemySprite, boolean playerSprite, boolean enemyFired, boolean hitbox){
		
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
		this.playerShot = fired;
		this.enemy = enemySprite;
		this.player = playerSprite;
		this.enemyShot = enemyFired;
		this.playerHitbox = hitbox;
	}
	
	// Moves Entity
	public void move(long delta){
		if(enemyShot == true){
			x += pattern.sineWaveX(delta / 50)/10000;
			y += (delta * dy)/1000;
		
		}
		else{
		// Update locations
			x += (delta * dx)/1000;
			y += (delta * dy)/1000;
		}
		
	}
	
	// Draw entity on graphics
	public void draw(Graphics g){
		
		sprite.draw(g, (int) x, (int) y);
		
	}
	
	// Checks for collision between entities
	public boolean collidesWith(Entity other){
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(him);
	}
	
	public abstract void collidedWith(Entity other);
	
	public void doLogic(){
	}
	
	// Left/Right movements
	public void setHorizontalMovement(double dx){
		this.dx = dx;
	}
	
	// Up/Down movements
	public void setVerticalMovement(double dy){
		this.dy = dy;
	}
	
	// Getter helper methods
	public double getHorizontalMovement(){
		return dx;
	}
	
	public double getVeritcalMovement(){
		return dy;
	}
	
	public int getX(){
		return (int) x;
	}
	
	public int getY(){
		return (int) y;
	}
	
	public boolean checkEnemy(){
		return this.enemy;
	}
	
	public boolean checkPlayer(){
		return this.player;
	}
	
	public boolean checkEnemyShot(){
		return this.enemyShot;
	}
	
	public boolean checkPlayerShot(){
		return this.playerShot;
	}
	
	public boolean checkPlayerHitbox(){
		return this.playerHitbox;
	}
	
	public int getPlayerHealth(){
		return 100;
	}
	
	public void reduceHealth(int health){
	}
	
	public int getEnemyCrashDmg(){
		return this.enemyCrashDmg;
	}
}
