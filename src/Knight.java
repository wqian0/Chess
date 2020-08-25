import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class Knight extends Piece {

	public Knight(boolean isWhite, Location loc, boolean hasMoved, boolean createImage) {
		super(isWhite, loc, hasMoved);
		if(createImage) {
			try {
				Image image;
				if(isWhite) {
					image = ImageIO.read(new File("files/w_knight.png"));
				}
				else {
					image = ImageIO.read(new File("files/b_knight.png"));
				}
				image = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				setImage(image);
			}
			catch(IOException e) {

			}
		}
	}

	public Set<Location> getMoves(Piece[][] board, boolean filter) { 
		Set<Location> result = new TreeSet<>();

		int x = getLoc().getX();
		int y = getLoc().getY();

		result.add(new Location(x - 2, y - 1));
		result.add(new Location(x - 2, y + 1));
		result.add(new Location(x + 2, y - 1));
		result.add(new Location(x + 2, y + 1));
		result.add(new Location(x - 1, y + 2));
		result.add(new Location(x - 1, y - 2));
		result.add(new Location(x + 1, y + 2));
		result.add(new Location(x + 1, y - 2));

		Iterator<Location> it = result.iterator();

		Location l;
		while(it.hasNext()) {
			l = it.next();
			if(!l.isValid()) {
				it.remove();
			}
			else if(!(board[l.getX()][l.getY()] instanceof EmptyPiece) 
					&& board[l.getX()][l.getY()].isWhite() == isWhite()) {
				it.remove();
			}
		}
		if(filter) {
			filterChecks(board, result);
		}

		return result;
	}

	public Piece getClone(Location l, boolean createImage) {
		return new Knight(isWhite(), l, hasMoved(), createImage);
	}


	public char getCharacter() {
		if(isWhite()) {
			return 'N';
		}
		return 'n';
	}


}
