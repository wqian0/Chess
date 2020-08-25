/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Scanner;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    private File selectedFile;
    private Scanner in = new Scanner(System.in);
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(560, 560);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status.setFont(new Font(status.getFont().getFontName(), Font.PLAIN, 16));
        
        status_panel.add(status);



        // Main playing area
        final ChessCourt court = new ChessCourt(status, in);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        
        final JPanel east_panel = new JPanel();
        frame.add(east_panel, BorderLayout.EAST);
        
        final JButton load = new JButton("Load from file");
        
        
        load.addActionListener(new ActionListener() {
        	   public void actionPerformed(ActionEvent e) {
        	      JFileChooser chooser = new JFileChooser();
        	      int returnValue = chooser.showOpenDialog(null);
        	      if (returnValue == JFileChooser.APPROVE_OPTION) {
        	          selectedFile = chooser.getSelectedFile();
        	          JOptionPane.showMessageDialog(frame, court.getGame().readBoardFromFile(selectedFile));
        	      }
        	   }
        	});
        control_panel.add(load);
        
        final JButton save = new JButton("Save to file");
        
        save.addActionListener(new ActionListener() {
     	   public void actionPerformed(ActionEvent e) {
     	      JFileChooser chooser = new JFileChooser();	
     	      
     	      int returnValue = chooser.showSaveDialog(null);
     	      if (returnValue == JFileChooser.APPROVE_OPTION) {
     	          selectedFile = chooser.getSelectedFile();
     	          court.getGame().writeBoardToFile(selectedFile);
     	      }
     	   }
     	});
        control_panel.add(save);
        
        final JButton instructions = new JButton("Instructions");
        
        instructions.addActionListener(e -> {
        	JOptionPane.showMessageDialog(frame, 
        			" MISCELLANEOUS:"
        			+ "\n 1) Select a piece by clicking on it. "
        			+ "\n 2) Move a selected piece by clicking on the desired target square."
        			+ "\n 3) Save and continue a game from a position by using the save and load buttons. "
        			+ "\n 4) Reset the game using the reset button."
        			+ "\n \n PIECES: \n "
        			+ "1) Pawn: Moves forward one square at a time, and can move two squares forward"
        			+ " \n if they are at their starting square. Captures diagonally. They can also capture"
        			+ " via en passant."
        			+ " \n When they reach the other end of the board, they are promoted into a piece of "
        			+ "the player's \n choice, decided by entering the first letter of the desired piece into the console. "
        			+ "\n 2) Bishop: Moves and captures diagonally."
        			+ "\n 3) Knight: Moves and captures in an L-shape. Can jump over pieces."
        			+ "\n 4) Rook: Moves and captures vertically or horizontally."
        			+ "\n 5) Queen: Moves and captures diagonally, vertically, or horizontally."
        			+ "\n 6) King: Moves and captures one square in any direction. Can make a special move known as castling "
        			+ " \n if it has not been moved, the rook on its right has not been moved, and there are no pieces in"
        			+ "\n between them. The same applies for the rook on its left. Castle by selecting the king and clicking "
        			+ "\n on the square two to its left/right."
        			+ "\n \n RULES: "
        			+ "\n 1) Neither player may capture their own pieces. "
        			+ "\n 2) A player is in check when their king is attacked by a piece of the opposite color. A player cannot"
        			+ "\n move into check, and a player in check must make a move that resolves the check. "
        			+ "\n 3) A player may not castle through check or while in check."
        			+ "\n 4) If a player is in check and cannot make any moves to resolve the check, the player has been"
        			+ "\n checkmated, and loses the game."
        			+ "\n 5) If a player is not in check but has no legal moves, the player has been stalemated, and the game"
        			+ "\n is a draw."
        			+ "\n 6) If the same position appears on the board 3 times, the game is a draw by three-fold repetition."
        			+ "\n 7) If neither player has made a pawn move or a capture in over 50 moves, the game is a draw by the "
        			+ "\n 50-move rule. "
        			+ "\n \n OBJECTIVE: Checkmate your opponent."
        			, 
        			 "Instructions", JOptionPane.INFORMATION_MESSAGE);
        });
        control_panel.add(instructions);
        
        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}