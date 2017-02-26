package TouhouFangame;

public class ShotEntity extends Entity{

	private Game game;
	private double moveSpeed = -500;
	private double moveEnemySpeed = 140;
	private boolean used = false;
	private boolean enemyFire = false;
	private int enemyDamage = 10;
	private int playerDamage = 10;
	private int numShot;
	
	public ShotEntity(Game game, String sprite, int x, int y, boolean enemyShot, int number){
		
		super(sprite,x,y, true, false, false, enemyShot, false);
		this.game = game;
		this.enemyFire = enemyShot;
		this.numShot = number;
		if(enemyShot == false){
			dy = moveSpeed;
		}
		else{
			dy = moveEnemySpeed;
		}
	}
	
	public void move(long delta, int number){
		
		// Normal moving
		
		super.move(delta);
		
		// If it goes past screen, remove entity
		if(y < -100 && y > 800){
			
			game.removeEntity(this);
			
		}
	}
	
	public int getPlayerDamage(){
		return this.playerDamage;
	}
	
	public int getEnemyDamage(){
		return this.enemyDamage;
	}
	
	public void collidedWith(Entity other){
		// If the shot already hit, do not use anymore
		if(used){
			return;
		}
		
		if(enemyFire == false && other instanceof EnemyEntity){
			game.removeEntity(this);
			game.removeEntity(other);
			game.notifyAlienKilled();
			used = true; 
		}
		
		if(enemyFire == true && other instanceof PlayerEntity){
			//game.notifyDeath();
			game.removeEntity(this);
			if(game.getPlayerHealthBar() == 10){
				//System.out.println(game.getPlayerHealthBar());
				game.setPlayerDeathHealth();
				game.notifyDeath();
			}
			else{
				game.reducePlayerHealth(enemyDamage);
			}
		}
	}
}
