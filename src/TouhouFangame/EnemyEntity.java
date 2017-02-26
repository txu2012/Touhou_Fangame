package TouhouFangame;

public class EnemyEntity extends Entity{
	
	private Game game;
	
	public EnemyEntity(Game game, String ref, int x, int y){
		
		super(ref,x,y, false, true, false, false, false);
		this.game = game;
		
	}
	
	public void move(long delta, int number){
		
		// If reaches left side, use updateLogic
		if(dx < 0 && x < 50){
			
			game.updateLogic();
			
		}
		
		// Same for right side
		if(dx > 0 && x > 750){
			
			game.updateLogic();
			
		}
		
		// Move alien
		super.move(delta);
	}
	
	public void doLogic(){
		
		// Invert x movement and move down y position
		dx = -dx;
		y += 10;
		
		// If alien reaches player, player dies
		if(y > 570){
			
			game.notifyDeath();
			
		}
		
	}
	
	public void collidedWith(Entity other){
	}

}
