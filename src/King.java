import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class King extends Piece {

	public King(boolean isWhite, Location loc, boolean hasMoved, boolean createImage){
		super(isWhite, loc, hasMoved);
		if(createImage) {
		try {
			Image image;
			if(isWhite) {
				image = ImageIO.read(new File("files/w_king.png"));
			}
			else {
				image = ImageIO.read(new File("files/b_king.png"));
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

		for(int i = -1; i <= 1; i++) 
		{
			for(int j = -1; j <= 1; j++) {
				result.add(new Location (x + i, y + j));
			}
		}
		result.remove(new Location(x ,y));

		Iterator<Location> it = result.iterator();

		Location l;
		while(it.hasNext()) {
			l = it.next();
			if(!l.isValid() || !(board[l.getX()][l.getY()] instanceof EmptyPiece
					|| board[l.getX()][l.getY()].isWhite() != isWhite())) {
				//System.out.println(l.getX() + "\t" + l.getY() + "UGH");
				it.remove();
			}
		}

		int rankY = 7;
		if(!isWhite()) {
			rankY = 0;
		}
		//castling
		if(!hasMoved()) {
			Piece[][] hypothetical = ChessGame.cloneBoard(board);
			if(board[7][rankY] instanceof Rook &&  //kingside
					!board[7][rankY].hasMoved()) {
				if(board[5][rankY] instanceof EmptyPiece && 
						board[6][rankY] instanceof EmptyPiece) {
					King clone = new King(isWhite(), new Location(5, rankY), true, false);
					hypothetical[x][y] = ChessGame.makeEmptyPiece(x, y);
					hypothetical[5][rankY] = clone;
					if(!ChessGame.playerChecked(hypothetical, isWhite())) {
						result.add(new Location(6, rankY)); 
					}
					//Game will have to deal with moving rook during castling
				}
			}
			hypothetical = ChessGame.cloneBoard(board);
			if(board[0][rankY] instanceof Rook && //queenside
					!board[0][rankY].hasMoved()) {
				if(board[1][rankY] instanceof EmptyPiece && 
						board[2][rankY] instanceof EmptyPiece && 
						board[3][rankY] instanceof EmptyPiece) {
					King clone = new King(isWhite(), new Location(3, rankY), true, false);
					hypothetical[x][y] = ChessGame.makeEmptyPiece(x, y);
					hypothetical[3][rankY] = clone;
					if(!ChessGame.playerChecked(hypothetical, isWhite())) {
						result.add(new Location(2, rankY)); 
					}
				}
			}
		}

		if(filter) {
			filterChecks(board, result);
		}

		return result;
	}
	public Piece getClone(Location l, boolean createImage) {
		return new King(isWhite(), l, hasMoved(), createImage);
	}


	public char getCharacter() {
		if(isWhite()) {
			return 'K';
		}
		return 'k';
	}
}
