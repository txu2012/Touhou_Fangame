package TouhouFangame;

public class PlayerEntity extends Entity{
	
	private Game game;
	private boolean playerHitbox = true;
	private int healthbar;
	
	public PlayerEntity(Game game, String ref, int x, int y, boolean Hitbox){
		
		super(ref,x,y, false, false, true, false, Hitbox);
		this.playerHitbox = Hitbox;
		this.game = game;
		if(Hitbox == true){
			this.healthbar = 100;
		}
	}
	
	public void move(long delta, int number){
		
		// If reaches side of left screen, do not move
		if(dx < 0 && x < 10 && playerHitbox == false){
			return;
		}
		
		// Similar for right side
		if(dx > 0 && x > 750 && playerHitbox == false){
			return;
		}
		
		// Top
		if(dy < 0 && y < 30 && playerHitbox == false){
			return;
		}
		
		// Bottom
		if(dy > 0 && y > 550 && playerHitbox == false){
			return;
		}
		
		// Hitbox
		// Left Side
		if(dx < 0 && x < 18 && playerHitbox == true){
			return;
		}

		// Similar for right side
		if(dx > 0 && x > 757 && playerHitbox == true){
			return;
		}
		
		// Top 
		if(dy < 0 && y < 50 && playerHitbox == true){
			return;
		}
		// Bottom
		if(dy > 0 && y > 570 && playerHitbox == true){
			return;
		}
		
		super.move(delta);
	}
	
	public void collidedWith(Entity other){
		if(playerHitbox == true && (other instanceof EnemyEntity || other instanceof ShotEntity)){
			if(other instanceof EnemyEntity){
				game.reducePlayerHealth(game.enemyDamage());
				game.removeEntity(other);
			}
			if(healthbar <= 10){
				game.notifyDeath();
				game.setPlayerDeathHealth();
			}
		}
	}
	
	public boolean checkHitbox(){
		return this.playerHitbox;
	}
	
	@Override
	public int getPlayerHealth(){
		return this.healthbar;
	}
	
	public void reduceHealth(int health){
		this.healthbar = this.healthbar - health;
	}
}
