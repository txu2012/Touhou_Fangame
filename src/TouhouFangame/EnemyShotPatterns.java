package TouhouFangame;

public class EnemyShotPatterns {
	private double x;
	private double y;
	
	public EnemyShotPatterns(){
	}
	
	public double sineWaveX(long xLoc){
		x = 20000 * Math.sin(xLoc);
		return x;
	}

}
