import java.util.Random;

public enum Direction {
	PLAYER,
	CENTER,
	RIGHTUP,
	LEFTUP,
	RIGHTBOTTOM,
	LEFTBOTTOM;
	
	//return une direction aleatoire 
	public static Direction randomDirection() {
	    int pick = new Random().nextInt(Direction.values().length);
	    return Direction.values()[pick];
	}
}

