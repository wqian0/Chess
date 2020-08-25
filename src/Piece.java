import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Set;

public abstract class Piece {
	
	private boolean isWhite;
	private Location loc;
	private boolean hasMoved;
	private Image image;
	
	public static final int size = 70;

	
	
	public Piece(boolean isWhite, Location loc, boolean hasMoved) {
		this.isWhite = isWhite;
		this.loc = loc;
		this.hasMoved = hasMoved;
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	
	public Location getLoc() {
		return loc;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public void setMoved(boolean input) {
		hasMoved = input;
	}
	
	public void filterChecks(Piece[][] board, Set<Location> input) {
		
		Piece[][] hypothetical = ChessGame.cloneBoard(board);
		
		int x = loc.getX();
		int y = loc.getY();
		
		Iterator <Location> it = input.iterator();
		Location next;
		while(it.hasNext()) {
			next = it.next();
			hypothetical[x][y] = ChessGame.makeEmptyPiece(x, y);
			hypothetical[next.getX()][next.getY()] = this.getClone(next, false);
			if(ChessGame.playerChecked(hypothetical, isWhite())) {
				it.remove();
			}
			hypothetical = ChessGame.cloneBoard(board);
		}
	}
	
	public void updateLoc(Location l) {
		loc = l;
	}
	
	public void setImage(Image input) {
		image = input;
	}
	
	public abstract Set<Location> getMoves(Piece[][] input, boolean filter);
	
	public abstract Piece getClone(Location l, boolean createImage);
	
	public abstract char getCharacter(); 
	
	public void draw(Graphics g) {
		g.drawImage(image, loc.getX()*size, loc.getY()*size, null);
	}

}
