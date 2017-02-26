package TouhouFangame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import java.awt.*;

public class Game extends Canvas{
	
	private BufferStrategy strategy;
	private boolean gameRunning = true;
	private ArrayList playerEntities = new ArrayList();
	private ArrayList entities = new ArrayList();
	private ArrayList removeList = new ArrayList();
	private Entity player;
	private Entity playerHitbox;
	private int enemyCount;
	private String message = "";
	private boolean doGameLogic = false;
	private boolean waitingKeyPress = true;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean firePressed = false;
	private double moveSpeed = 300;
	private long lastFire = 0;
	private long lastEnemyFire = 0;
	private long firingInterval = 100;
	private long firingEnemy = 400;
	private JFrame container = new JFrame("Touhou Shooter");
	private String windowTitle = "Touhou Shooter";
	private long lastFpsTime;
	private int fps;
	private int playerHealthBar;
	private int enemyCrashDamage;
	private Graphics2D g;
	boolean enemyShoot = false;
	boolean died = false;

	public Game(){
		// Creates the frame to hold the game
		container = new JFrame("Touhou Shooter");
		
		// Grabs content and set resolution of game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(1000,600));
		panel.setLayout(null);
		
		// Set canvas size and put contents into frame
		setBounds(0,0,1000,600);
		panel.add(this);
		
		// Does not allow repainting canvas
		setIgnoreRepaint(true);
		
		// Sets window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		container.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		addKeyListener(new KeyInputHandler());
		requestFocus();
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		initEntities();
	}
	
	public void displayGame(){
		g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,1000,600);
		g.setColor(Color.gray);
		g.fillRect(800, 10, 5, 580);
	}
	
	private void startGame(){
		entities.clear();
		initEntities();
		
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		firePressed = false;
	}
	
	public void gameLoop(){
		long lastLoopTime = System.currentTimeMillis();
		
		Timer timer = new Timer();
		
	
		while(gameRunning){	
			// Calculates how far entities should move this loop
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			// Update Frame Counter
			lastFpsTime += delta;
			fps++;
			
			if(lastFpsTime >= 1000){
				container.setTitle(windowTitle + " (FPS: " + fps + ")");
				lastFpsTime = 0;
				fps = 0;
			}
			// Get graphics context and blank it out
			displayGame();
			
			// Move entity itself
			if(waitingKeyPress == false){
				for(int i = 0; i < entities.size(); i++){
					if(entities.get(i) != null){
						
						Entity entity = (Entity) entities.get(i);
						entity.move(delta);
					
					}
				}
				player.move(delta);
				playerHitbox.move(delta);
			}
			
			
			// Redraw the entity in new location
			for(int i = 0; i < entities.size();i++){
				if(entities.get(i) != null){
					Entity entity = (Entity) entities.get(i);
					entity.draw(g);
				}
			}
			player.draw(g);
			playerHitbox.draw(g);
			
			
			if(waitingKeyPress){
				leftPressed = false;
				rightPressed = false;
				upPressed = false;
				downPressed = false;
				firePressed = false;
				//died = true;
				g.setColor(Color.white);
				g.drawString(this.message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				g.drawString("Press any key", (800-g.getFontMetrics().stringWidth("Press any key"))/2, 300);
			}
			
			if(waitingKeyPress != true){
				timer.schedule(new TimerTask() {
					
					   public void run() {
						   enemyShoot = true;
					   }

					}, 2000);
			}

			entities.removeAll(removeList);
			removeList.clear();
			
			//for(int p = 0; p < playerEntities.size(); p++){
				for(int s = 0; s < entities.size(); s++){
					if(entities.get(s) != null){
						//Entity me = (Entity) playerEntities.get(p);
						Entity him = (Entity) entities.get(s);
						
						if(playerHitbox.collidesWith(him) && (playerHitbox.checkPlayerHitbox() == true && him.checkEnemy() == true) /*|| (me.checkEnemy() == true && him.checkPlayerHitbox() == true*/){
							playerHitbox.collidedWith(him);
							him.collidedWith(playerHitbox);
							break;
						}
						if(playerHitbox.collidesWith(him) && ((playerHitbox.checkPlayerHitbox() == true && him.checkEnemyShot() == true))){
							playerHitbox.collidedWith(him);
							him.collidedWith(playerHitbox);
						}/*
						if(me.collidesWith(him) && ((me.checkPlayerShot() == true && him.checkEnemy() == true) || (me.checkEnemy() == true && him.checkPlayerShot() == true))){
							me.collidedWith(him);
							him.collidedWith(me);
						}*/
					}
				}
			//}
			
			entities.removeAll(removeList);
			removeList.clear();
			
			// Game logic for aliens if reaches sides
			if(doGameLogic){
				for(int i = 0; i < entities.size(); i++){
					if(entities.get(i) != null){
						Entity entity = (Entity) entities.get(i);
						entity.doLogic();
					}
				}
				doGameLogic = false;
			}
			
			
			displaySide();
			// Completed drawing, clear graphics, flip buffer
			g.dispose();
			strategy.show();
			
			// Initialize movement to not move
			player.setHorizontalMovement(0);
			player.setVerticalMovement(0);
			playerHitbox.setHorizontalMovement(0);
			playerHitbox.setVerticalMovement(0);
			
			// If direction key is pressed, move that direction
			
			 if(leftPressed && upPressed && !rightPressed && !downPressed){
				player.setHorizontalMovement(-moveSpeed);
				player.setVerticalMovement(-moveSpeed);
				playerHitbox.setHorizontalMovement(-moveSpeed);
				playerHitbox.setVerticalMovement(-moveSpeed);
			}
			else if(leftPressed && downPressed && !rightPressed && !upPressed){
				player.setHorizontalMovement(-moveSpeed);
				player.setVerticalMovement(moveSpeed);
				playerHitbox.setHorizontalMovement(-moveSpeed);
				playerHitbox.setVerticalMovement(moveSpeed);
			}
			else if(rightPressed && upPressed && !leftPressed && !downPressed){
				player.setHorizontalMovement(moveSpeed);
				player.setVerticalMovement(-moveSpeed);
				playerHitbox.setHorizontalMovement(moveSpeed);
				playerHitbox.setVerticalMovement(-moveSpeed);
			}
			else if(rightPressed && downPressed && !leftPressed && !upPressed){
				player.setHorizontalMovement(moveSpeed);
				player.setVerticalMovement(moveSpeed);
				playerHitbox.setHorizontalMovement(moveSpeed);
				playerHitbox.setVerticalMovement(moveSpeed);
			}
			else if((leftPressed && !rightPressed && !upPressed && !downPressed) || (leftPressed && !rightPressed && upPressed && downPressed)){
				player.setHorizontalMovement(-moveSpeed);
				playerHitbox.setHorizontalMovement(-moveSpeed);
			}
			else if((!leftPressed && rightPressed && !upPressed && !downPressed) || (!leftPressed && rightPressed && upPressed && downPressed)){
				player.setHorizontalMovement(moveSpeed);
				playerHitbox.setHorizontalMovement(moveSpeed);
			}
			else if((!upPressed && downPressed && !leftPressed && !rightPressed) || (!upPressed && downPressed && leftPressed && rightPressed)){
				player.setVerticalMovement(moveSpeed);
				playerHitbox.setVerticalMovement(moveSpeed);
			}
			else if((upPressed && !downPressed && !leftPressed && !rightPressed) || (upPressed && !downPressed && leftPressed && rightPressed)){
				player.setVerticalMovement(-moveSpeed);
				playerHitbox.setVerticalMovement(-moveSpeed);
			}
			
			
			if(firePressed){
				tryToFire();
			}
			
			if(enemyShoot){
				enemyFire();
			}
			// Pause using thread sleeping
			try{
				Thread.sleep(10);
			}
			catch(Exception e){}
			
		}
		
		if(died){
			gameRunning = true;
			enemyShoot = false;
			died = false;
			gameLoop();
		}

	}
	
	private void initEntities(){
		// Create Ship at center
		player = new PlayerEntity(this,"sprites/chen.png",370,550, false);
		//playerEntities.add(player);
		playerHitbox = new PlayerEntity(this,"sprites/Hitbox.png", 378,570, true);
		playerHealthBar = playerHitbox.getPlayerHealth();
		//System.out.println(playerHealthBar);
		//playerEntities.add(playerHitbox);
		
		// Create enemies
		enemyCount = 0;
		//Entity alien = new EnemyEntity(this,"sprites/mochien.png",100+(3*50),(50)+3*30);
		Entity alien = new EnemyEntity(this,"sprites/mochien.png",380,70);
		enemyCrashDamage = alien.getEnemyCrashDmg();
		alien.setHorizontalMovement(0);
		entities.add(alien);
		enemyCount++;
		/*
		for(int row = 0; row < 5; row++){
			for(int x = 0; x < 12; x++){
				Entity enemy = new EnemyEntity(this,"sprites/mochien.png",100+(x*50),(50)+row*30);
				enemy.setHorizontalMovement(100);
				entities.add(enemy);
				enemyCount++;
				enemyCrashDamage = enemy.getEnemyCrashDmg();
			}
		}*/
	}
	
	public void displaySide(){		
		g.setColor(Color.white);
		g.drawString("Health: ",860-g.getFontMetrics().stringWidth("Health: "),50);
		g.drawString(String.valueOf(this.playerHealthBar),860,50);	
	}
	
	// Removes entity from game
	public void removeEntity(Entity entity){
		removeList.add(entity);
	}
	
	public void updateLogic(){
		doGameLogic = true;
	}
	
	public void notifyDeath(){
		message = "You died. Try again?";
		waitingKeyPress = true;
		gameRunning = false;
		died = true;
	}
	
	public void notifyAlienKilled(){
		enemyCount--;
		
		// If no more alien entities, win game.
		if(enemyCount == 0){
			notifyWin();
		}
		
		// Find all alien entities and speed up by 2%
		for(int i = 0; i < entities.size(); i++){
			Entity entity = (Entity) entities.get(i);
			
			// Increase speed by 2%
			if(entity instanceof EnemyEntity){
				entity.setHorizontalMovement(entity.getHorizontalMovement());
			}
		}
	}
	
	public void notifyWin(){
		message = "You win!";
		waitingKeyPress = true;
		gameRunning = false;
	}
	
	public void tryToFire(){
		// Check waiting time for fire
		if(System.currentTimeMillis() - lastFire < firingInterval){
			//fired = false;
			return;
		}
		
		// If waited long enough, add shot entity, record last time shot
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/karaage.png", player.getX(), player.getY()-30, false, 0);
		entities.add(shot);
		//fired = true;
	}
	
	public void enemyFire(){
		if(System.currentTimeMillis() - lastEnemyFire < firingEnemy){
			return;
		}
		
		lastEnemyFire = System.currentTimeMillis();
		int k = 1;
		for(int i = 0; i < entities.size(); i++){
			Entity entity = (Entity) entities.get(i);
			if(entity.checkEnemy() == true){
				for(int j = 0;j<5;j++){
					ShotEntity enemyShot = new ShotEntity(this, "sprites/redball.png", entity.getX(), entity.getY(), true, k);
					entities.add(enemyShot);
					k++;
				}
			}
		}
		k = 1;
		
	}
	
	public int getPlayerHealthBar(){
		return this.playerHealthBar;
	}
	
	public void reducePlayerHealth(int health){
		playerHitbox.reduceHealth(health);
		this.playerHealthBar = playerHitbox.getPlayerHealth();
	}
	
	public void setPlayerDeathHealth(){
		this.playerHealthBar = 0;
	}
	
	public int enemyDamage(){
		return this.enemyCrashDamage;
	}
	
	private class KeyInputHandler extends KeyAdapter{
		private int pressCount = 1;
		
		public void keyPressed(KeyEvent e){
			// Register key presses
			if(waitingKeyPress){
				return;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftPressed = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightPressed = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_UP){
				upPressed = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				downPressed = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				firePressed = true;
			}
		}
		
		public void keyReleased(KeyEvent e){
			// Register key releases
			if(waitingKeyPress){
				return;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftPressed = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightPressed = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_UP){
				upPressed = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				downPressed = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				firePressed = false;
			}
		}
		
		public void keyTyped(KeyEvent e){
			// Exit Game if esc is pressed
			if(waitingKeyPress){
				if(pressCount == 1){
					waitingKeyPress = false;
					startGame();
					pressCount = 0;
				}
				else{
					pressCount++;
				}
			}
			
			if(e.getKeyChar() == 27){
				System.exit(0);
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		Game g = new Game();
		g.gameLoop();
	}

}
