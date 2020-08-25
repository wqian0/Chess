import java.util.Scanner;

public class ChessMain {
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		ChessGame g = new ChessGame(in);
		
		g.makeMove(new Location(4, 6), new Location(4, 4)); //e4
		g.makeMove(new Location(4, 1), new Location(4, 3)); //e5
		g.makeMove(new Location(5, 7), new Location(2, 4)); //Bc4 
		g.makeMove(new Location(5, 1), new Location(5, 3)); //f5
		g.makeMove(new Location(3, 7), new Location(7, 3)); //Qh5+
		g.makeMove(new Location(4, 0), new Location(4, 1)); //Ke7
		g.makeMove(new Location(4, 4), new Location(5, 3)); //exf5
		g.makeMove(new Location(3, 1), new Location(3, 2)); //d6
		g.makeMove(new Location(0, 6), new Location(0, 5)); //a3
		g.makeMove(new Location(2, 0), new Location(3, 1)); //Bd7
		g.makeMove(new Location(7, 3), new Location(5, 1)); //Qf7#
		
		char[][] charArray = g.getCharBoard();
		
		System.out.print("\t");
		for(int i = 0; i < 8; i++) {
			System.out.print(i +"\t");
		}
		System.out.println();
		for(int i = 0; i < 8; i++) {
			System.out.print(i +"\t");
			for(int j = 0; j < 8; j++) {
				System.out.print(charArray[j][i] + "\t");
			}
			System.out.println();
		}
		
		in.close();
	}
}
