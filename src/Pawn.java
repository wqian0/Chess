import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class Pawn extends Piece{

	private boolean epAble; //can be en-passanted
	public Pawn(boolean isWhite, Location loc, boolean hasMoved, boolean createImage) {
		super(isWhite, loc, hasMoved);
		epAble = false;
		Image image;
		if(createImage) {
			try {
				if(isWhite) {
					image = (ImageIO.read(new File("files/w_pawn.png")));
				}
				else {
					image = ImageIO.read(new File("files/b_pawn.png"));
				}
				image = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				setImage(image);
			}
			catch (IOException e) {

			}
		}
	}

	public boolean isEpAble() {
		return epAble;
	}

	public void setEpAbleStatus(boolean input) { //needs to be used by the game class; check if a pawn move was two up
		epAble = input; 
	}
	public Set<Location> getMoves(Piece[][] board, boolean filter) {
		Set<Location> result = new TreeSet<Location>();

		int sign = 1;
		int x = getLoc().getX();
		int y = getLoc().getY();


		if(!isWhite()) {
			sign = -1;
		}

		Location l = new Location(x, y - sign);

		if(l.isValid() && board[l.getX()][l.getY()] instanceof EmptyPiece) {
			l.setVertPawnMove();
			result.add(l);
			l = new Location(x, y - 2 * sign);
			if(l.isValid() &&!hasMoved() && board[l.getX()][l.getY()] instanceof EmptyPiece) {
				l.setVertPawnMove();
				result.add(l);
			}
		}

		l = new Location(x - 1, y - sign);
		if(l.isValid()) {
			if (!(board[l.getX()][l.getY()] instanceof EmptyPiece)
					&& board[l.getX()][l.getY()].isWhite() != isWhite()) {
				result.add(l);
			}
			else if(board[l.getX()][y] instanceof Pawn && 
					board[l.getX()][y].isWhite() != isWhite()) {
				Pawn p = (Pawn) board[l.getX()][y];
				if(p.isEpAble()) {
					result.add(l);
				}
			}
		}

		l = new Location(x + 1, y - sign);
		if(l.isValid()) {
			if (!(board[l.getX()][l.getY()] instanceof EmptyPiece)
					&& board[l.getX()][l.getY()].isWhite() != isWhite()) {
				result.add(l);
			}
			else if(board[l.getX()][y] instanceof Pawn && 
					board[l.getX()][y].isWhite() != isWhite()) {
				Pawn p = (Pawn) board[l.getX()][y];
				if(p.isEpAble()) {
					result.add(l);
				}
			}
		}
		if(filter) {
			filterChecks(board, result);
		}

		return result;
	}

	public Piece getClone(Location l, boolean createImage) {
		return new Pawn(isWhite(), l, hasMoved(), createImage);
	}

	public char getCharacter() {
		if(isWhite()) {
			return 'P';
		}
		return 'p';
	}
}
