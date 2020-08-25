import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class Queen extends Piece {

	public Queen(boolean isWhite, Location loc, boolean hasMoved, boolean createImage) {
		super(isWhite, loc, hasMoved);
		if(createImage) {
			try {
				Image image;
				if(isWhite) {
					image = ImageIO.read(new File("files/w_queen.png"));
				}
				else {
					image = ImageIO.read(new File("files/b_queen.png"));
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

		for(int i = x - 1; i >= 0; i--) {
			if(board[i][y] instanceof EmptyPiece) {
				result.add(new Location(i, y));
			}
			else if(board[i][y].isWhite() != isWhite()) {
				result.add(new Location(i, y));
				break;
			}
			else {
				break;
			}
		}

		for(int i = x + 1; i < 8; i++) {
			if(board[i][y] instanceof EmptyPiece) {
				result.add(new Location(i, y));
			}
			else if(board[i][y].isWhite() != isWhite()) {
				result.add(new Location(i, y));
				break;
			}
			else {
				break;
			}
		}

		for(int i = y - 1; i >= 0; i--) {
			if(board[x][i] instanceof EmptyPiece) {
				result.add(new Location(x, i));
			}
			else if(board[x][i].isWhite() != isWhite()) {
				result.add(new Location(x, i));
				break;
			}
			else {
				break;
			}
		}

		for(int i = y + 1; i < 8; i++) {
			if(board[x][i] instanceof EmptyPiece) {
				result.add(new Location(x, i));
			}
			else if(board[x][i].isWhite() != isWhite()) {
				result.add(new Location(x, i));
				break;
			}
			else {
				break;
			}
		}

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

		while(i >= 0 && j < 8) {
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
		return new Queen(isWhite(), l, hasMoved(), createImage);
	}


	public char getCharacter() {
		if(isWhite()) {
			return 'Q';
		}
		return 'q';
	}

}
