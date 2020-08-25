
public class Location implements Comparable<Location>{
	private int x;
	private int y;
	private boolean valid;
	private boolean vertPawnMove; //This is useful for deleting vertical pawn moves from the list of possible checks
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
		valid = false;
		vertPawnMove = false;
		if(x >= 0 && x <=7 && y >= 0 && y<=7) {
			valid = true;
		}
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public void setVertPawnMove() {
		vertPawnMove = true;
	}

	public boolean isVertPawnMove() {
		return vertPawnMove;
	}
	public boolean isValid() {
		return valid;
	}
	
	@Override
	public boolean equals (Object other) {
		if (other == null) {
			return false;
		}
		
		if(!(other instanceof Location)) {
			return false;
		}
		
		Location otherL = (Location) other;
		return x == otherL.getX() && y == otherL.getY();
	}
	
	public int compareTo (Location other) {
		return ((Integer)((x * 0x1f1f1f1f) ^ y))
				.compareTo((Integer)(other.getX() * 0x1f1f1f1f ^ other.getY()));
	}
}
