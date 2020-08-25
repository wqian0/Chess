import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class Bishop extends Piece {

	public Bishop(boolean isWhite, Location loc, boolean hasMoved, boolean createImage) {
		super(isWhite, loc, hasMoved);
		if(createImage) {
			try {
				Image image;
				if(isWhite) {
					image = ImageIO.read(new File("files/w_bishop.png"));
				}
				else {
					image = ImageIO.read(new File("files/b_bishop.png"));
				}
				image = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				setImage(image);
			}
			catch(IOException e) {

			}
		}
	}


	public Set<Location> getMoves(Piece[][] board, boolean filter) {
		Set<Location> result = new TreeSet<Location>();

		int x = getLoc().getX();
		int y = getLoc().getY();

		int i = x - 1;
		int j = y - 1;

		while(i >= 0 && j >= 0) {
			if(board[i][j] instanceof EmptyPiece) {
				result.add(new Location(i, j));
			}
			else if(board[i][j].isWhite() != isWhite()) {
				result.add(new Location(i, j));
				break;
			}
			else {
				break;
			}
			i--;
			j--;
		}

		i = x - 1;
		j = y + 1;

		while(i >= 0 && j<8) {
			if(board[i][j] instanceof EmptyPiece) {
				result.add(new Location(i, j));
			}
			else if(board[i][j].isWhite() != isWhite()) {
				result.add(new Location(i, j));
				break;
			}
			else {
				break;
			}
			i--;
			j++;
		}

		i = x + 1;
		j = y - 1;

		while(i < 8 && j >= 0) {
			if(board[i][j] instanceof EmptyPiece) {
				result.add(new Location(i, j));
			}
			else if(board[i][j].isWhite() != isWhite()) {
				result.add(new Location(i, j));
				break;
			}
			else {
				break;
			}
			i++;
			j--;
		}

		i = x + 1;
		j = y + 1;

		while(i < 8 && j < 8) {
			if(board[i][j] instanceof EmptyPiece) {
				result.add(new Location(i, j));
			}
			else if(board[i][j].isWhite() != isWhite()) {
				result.add(new Location(i, j));
				break;
			}
			else {
				break;
			}
			i++;
			j++;
		}


		if(filter) {
			filterChecks(board, result);
		}

		return result;

	}

	public Piece getClone(Location l, boolean createImage) {
		return new Bishop(isWhite(), l, hasMoved(), createImage);
	}


	public char getCharacter() {
		if(isWhite()) {
			return 'B';
		}
		return 'b';
	}

}
