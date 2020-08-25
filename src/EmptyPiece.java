import java.util.Set;
import java.util.TreeSet;

public class EmptyPiece extends Piece{

	public EmptyPiece(boolean isWhite, Location loc, boolean hasMoved) {
		super(isWhite, loc, hasMoved);
	}
	
	public Set<Location> getMoves(Piece[][] board, boolean filter) {
		Set<Location> result = new TreeSet<Location>();
		return result;
	}
	
	public Piece getClone(Location l, boolean createImage) {
		return new EmptyPiece(isWhite(), l, hasMoved());
	}
	
	public char getCharacter() {
		return '-';
	}
}
