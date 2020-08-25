import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ChessGame {
	private Piece[][] board;
	private Player white;
	private Player black;
	private boolean whiteToPlay;
	private List<Integer> hashCodes; // Check for 3-fold repetition
	private Scanner in;
	private int moveNum; 
	private int lastPawnOrCapMove; // Check for the 50-move rule
	
	public enum Outcome {
		IN_PROGRESS,
		WHITE_WINS,
		BLACK_WINS,
		DRAW
	}

	private Outcome winner;

	public ChessGame(Scanner in) {
		this.in = in;
		board = new Piece[8][8];
		setUpBoard();
		whiteToPlay = true;
		hashCodes = new ArrayList<Integer>();
		hashCodes.add(boardHashCode());
		moveNum = 0;
		lastPawnOrCapMove = 0;
		winner = Outcome.IN_PROGRESS;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public void setUpBoard() {
		for(int i = 0; i < 8; i++) {
			board[i][6] = new Pawn(true, new Location(i, 6), false, true);
			board[i][1] = new Pawn(false, new Location(i, 1), false, true);
		}

		board[0][7] = new Rook(true, new Location(0, 7), false, true);
		board[7][7] = new Rook(true, new Location(7, 7), false, true);
		board[1][7] = new Knight(true, new Location(1, 7), false, true);
		board[6][7] = new Knight(true, new Location(6, 7), false, true);
		board[2][7] = new Bishop(true, new Location(2, 7), false, true);
		board[5][7] = new Bishop(true, new Location(5, 7), false, true);
		board[3][7] = new Queen(true, new Location(3, 7), false, true);
		board[4][7] = new King(true, new Location(4, 7), false, true);

		board[0][0] = new Rook(false, new Location(0, 0), false, true);
		board[7][0] = new Rook(false, new Location(7, 0), false, true);
		board[1][0] = new Knight(false, new Location(1, 0), false, true);
		board[6][0] = new Knight(false, new Location(6, 0), false, true);
		board[2][0] = new Bishop(false, new Location(2, 0), false, true);
		board[5][0] = new Bishop(false, new Location(5, 0), false, true);
		board[3][0] = new Queen(false, new Location(3, 0), false, true);
		board[4][0] = new King(false, new Location(4, 0), false, true);

		for(int i = 0 ; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] == null){
					board[i][j] = makeEmptyPiece(i, j);
				}
			}
		}
	}

	public char[][] getCharBoard() {
		char[][] result = new char[8][8];

		for(int i = 0; i < 8; i++) 
		{
			for(int j = 0; j < 8; j++) {
				result[i][j] = board[i][j].getCharacter();
			}
		}
		return result;
	}
	
	public int getMoveNum() {
		return moveNum;
	}
	
	public int boardHashCode() {
		char[][] charBoard = getCharBoard();
		int result = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				result += (i + 1) * (j + 1) * ((Character) charBoard[i][j]).hashCode();
			}
		}
		return result;
	}
	
	public static Piece[][] cloneBoard(Piece[][] board) {
		Piece[][] result = new Piece[8][8];
		for(int i = 0; i < 8; i++) {
			result[i] = board[i].clone();
		}
		return result;
	}
	
	public void writeBoardToFile(File f) {
		try {
			PrintWriter pw = new PrintWriter(f);
			char[][] board = getCharBoard();
			if(whiteToPlay) {
				pw.println("W");
			}
			else {
				pw.println("B");
			}
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					pw.print(board[i][j] +"\t");
				}
				pw.println();
			}
			pw.close();
		} catch (FileNotFoundException e) {
			
		}
	}

	public Piece charToPiece(char c, int row, int col) {
		Location l = new Location(row, col);
		if(c == '-') {
			return new EmptyPiece(false, l, false);
		}
		else if(c == 'K') {
			return new King(true, l, false, true);
		}
		else if(c == 'Q') {
			return new Queen(true, l, false, true);
		}
		else if(c == 'R') {
			return new Rook(true, l, false, true);
		}
		else if(c == 'B') {
			return new Bishop(true, l, false, true);
		}
		else if(c == 'N') {
			return new Knight(true, l, false, true);
		}
		else if(c == 'P') {
			return new Pawn(true, l, false, true);
		}
		else if(c == 'k') {
			return new King(false, l, false, true);
		}
		else if(c == 'q') {
			return new Queen(false, l, false, true);
		}
		else if(c == 'r') {
			return new Rook(false, l, false, true);
		}
		else if(c == 'b') {
			return new Bishop(false, l, false, true);
		}
		else if(c == 'n') {
			return new Knight(false, l, false, true);
		}
		else if(c == 'p') {
			return new Pawn(false, l, false, true);
		}
		else {
			return null;
		}
	}

	public String readBoardFromFile(File f) {
		Piece[][] newBoard = new Piece[8][8];
		boolean startingPlayer = true;
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(f);
			
			if(!sc.hasNext()) {
				return "Invalid input file.";
			}
			String starter = sc.next();
			if(starter.equals("B")) {
				startingPlayer = false;
			}
			else if(!starter.equals("W")) {
				return "Invalid input file.";
			}
			
			char next = ' ';
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					if(!sc.hasNext()) {
						return "Invalid input file.";
					}
					next = sc.next().charAt(0);
					Piece p = charToPiece(next, i, j);
					if(p == null) {
						return "Invalid input file.";
					}
					else {
						newBoard[i][j] = p; 
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			return "Invalid input file.";
		}
		board = newBoard;
		moveNum = 0;
		lastPawnOrCapMove = 0;
		hashCodes = new ArrayList<Integer>();
		hashCodes.add(boardHashCode());
		whiteToPlay = startingPlayer;
		winner = Outcome.IN_PROGRESS;
		updateOutcome();
		return "Position loaded from file successfully.";
	}

	public Player getWhite() {
		return white;
	}

	public Player getBlack() {
		return black;
	}

	public boolean isWhiteToPlay() {
		return whiteToPlay;
	}

	public static Piece makeEmptyPiece(int x, int y) {
		return new EmptyPiece(false, 
				new Location(x, y), false);
	}

	public void updateOutcome() {
		if(playerCheckmated(true)) {
			winner = Outcome.BLACK_WINS;
			System.out.println("BLACK WINS!");
			return;
		}
		if(playerCheckmated(false)) {
			winner = Outcome.WHITE_WINS;
			System.out.println("WHITE WINS!");
			return;
		}
		if((playerStalemated(true) && whiteToPlay) ||
				playerStalemated(false) && !whiteToPlay) {
			winner = Outcome.DRAW;
			System.out.println("DRAW BY STALEMATE!");
			return;
		}
		int count = 0;
		for(Integer x: hashCodes) {
			if(x.equals(hashCodes.get(hashCodes.size() - 1))) {
				count++;
			}
		}
		if(count >= 3) {
			winner = Outcome.DRAW;
			System.out.println("DRAW BY 3-FOLD REPETITION!");
			return;
		}
		if(moveNum - lastPawnOrCapMove >= 50) {
			winner = Outcome.DRAW;
			System.out.println("DRAW BY 50-MOVE RULE!");
		}
	}

	public Outcome getOutcome() {
		return winner;
	}

	public boolean makeMove(Location start, Location end) {
		if(winner != Outcome.IN_PROGRESS) {
			return false;
		}
		Piece p = board[start.getX()][start.getY()];
		Piece target = board[end.getX()][end.getY()];
		if(p instanceof EmptyPiece || p.isWhite() != whiteToPlay) {
			return false;
		}
		Set<Location> moves = p.getMoves(board, true);
		if(!moves.contains(end)) {	
			return false;
		}

		board[start.getX()][start.getY()] = makeEmptyPiece(start.getX(), start.getY());
		board[end.getX()][end.getY()] = p;
		
		int rankY = 0;
		if(!whiteToPlay) {
			rankY = 7;
		}
		
		if(p instanceof Pawn) {
			if(Math.abs(start.getY()-end.getY()) == 2) { //moved two up, en-passantable
				((Pawn) p).setEpAbleStatus(true);
			}
			else if(Math.abs(start.getX()-end.getX())!=0 && 
					target instanceof EmptyPiece) { //en-passant
				board[end.getX()][start.getY()] = makeEmptyPiece(end.getX(), end.getY());
			}
			else if(end.getY() == rankY) {
				promotePawn(in, end);
			}
		}
		rankY = 7 - rankY;
		if(p instanceof King) {
			if(end.getX() - start.getX() == 2) { //kingside castle
				if(playerChecked(board, whiteToPlay)) {
					return false;
				}
				board[5][rankY] = board[7][rankY].getClone(new Location(5, rankY), true);
				board[7][rankY] = makeEmptyPiece(7, rankY);
			}
			if(start.getX() - end.getX() == 2) { //queenside castle
				if(playerChecked(board, whiteToPlay)) {
					return false;
				}
				board[3][rankY] = board[0][rankY].getClone(new Location(3, rankY), true);
				board[0][rankY] = makeEmptyPiece(0, rankY);
			}
		}
	
		p.setMoved(true);
		p.updateLoc(end);
		
		if(whiteToPlay) {
			moveNum++;
		}
		if(p instanceof Pawn || !(target instanceof EmptyPiece)) {
			lastPawnOrCapMove = moveNum;
		}
		hashCodes.add(boardHashCode());
		whiteToPlay = !whiteToPlay;
		updateEnPassant(whiteToPlay);
		updateOutcome();
		return true;
	}

	public void updateEnPassant(boolean player) { //en-passantable status expires after 1 move
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if(board[i][j] instanceof Pawn && 
						board[i][j].isWhite() == player) {
					((Pawn) board[i][j]).setEpAbleStatus(false);
				}
			}
		}
	}

	public void setScanner(Scanner input) {
		in = input;
	}
	public void promotePawn(Scanner input, Location target) {
		int x = target.getX();
		int y = target.getY();

		System.out.println("Type the capitalized first letter of the "
				+ "piece you want to promote to. Knights are denoted by an N. ");
		String next = input.next();
		if(next.equals("N")) {
			board[x][y] = new Knight(whiteToPlay, target, false, true);
		}
		else if(next.equals("B")) {
			board[x][y] = new Bishop(whiteToPlay, target, false, true);
		}
		else if(next.equals("R")) {
			board[x][y] = new Rook(whiteToPlay, target, false, true);
		}
		else if(next.equals("Q")) {
			board[x][y] = new Queen(whiteToPlay, target, false, true);
		}
		else {
			System.out.println("Incorrect input. Try again.");
			promotePawn(input, target);
		}
	}

	public static boolean playerChecked(Piece[][] input, boolean whitePlayer) {
		Piece king = new King(false, null, false, false);
		Set<Location> oppMoves = new TreeSet<Location>();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(input[i][j] instanceof King && 
						input[i][j].isWhite() == whitePlayer) {
					king = input[i][j];
				}
				if(!(input[i][j] instanceof EmptyPiece) 
						&& input[i][j].isWhite() != whitePlayer) {
					oppMoves.addAll(input[i][j].getMoves(input, false));
				}
			}
		}
		
		Iterator<Location> it = oppMoves.iterator();

		while(it.hasNext()) {
			if(it.next().isVertPawnMove()) {
				it.remove();
			}
		}
		
		if(oppMoves.contains(king.getLoc())) {
			return true;
		}
		return false;
	}

	public boolean playerStalemated(boolean whitePlayer) {

		Piece king = new King(false, null, false, false);
		Set<Location> myMoves = new TreeSet<Location>();
		Set<Location> oppMoves = new TreeSet<Location>();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(!(board[i][j] instanceof EmptyPiece)) {
					if(board[i][j].isWhite() == whitePlayer) {
						myMoves.addAll(board[i][j].getMoves(board, true));
					}
					else if(board[i][j].isWhite() != whitePlayer) {
						oppMoves.addAll(board[i][j].getMoves(board, false));
					}
					if(board[i][j] instanceof King && 
							board[i][j].isWhite() == whitePlayer) {
						king = board[i][j];
					}
				}
			}
		}
		
		Iterator<Location> it = oppMoves.iterator();

		while(it.hasNext()) {
			if(it.next().isVertPawnMove()) {
				it.remove();
			}
		}
		if(myMoves.size() == 0 && !oppMoves.contains(king.getLoc()))
			return true;
		return false;
	}

	public boolean playerCheckmated(boolean whitePlayer) {
		Piece king = new King(false, null, false, false);

		Set<Location> myMoves = new TreeSet<Location>();
		Set<Location> oppMoves = new TreeSet<Location>();

		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(!(board[i][j] instanceof EmptyPiece)) {
					if(board[i][j].isWhite() == whitePlayer) {
						myMoves.addAll(board[i][j].getMoves(board, true));
					}
					else if(board[i][j].isWhite() != whitePlayer) {
						oppMoves.addAll(board[i][j].getMoves(board, false));
					}
					if(board[i][j] instanceof King && 
							board[i][j].isWhite() == whitePlayer) {
						king = board[i][j];
					}
				}
			}
		}
		Iterator<Location> it = oppMoves.iterator();

		while(it.hasNext()) {
			if(it.next().isVertPawnMove()) {
				it.remove();
			}
		}

		if(oppMoves.contains(king.getLoc())) {
			if(king.getMoves(board, true).size() == 0 && myMoves.size() == 0) {
				return true;
			}
		}
		return false;

	}


}
