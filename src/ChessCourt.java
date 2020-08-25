/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class ChessCourt extends JPanel {

    // the state of the game logic

	private ChessGame game;
	private boolean pieceSelected;
	private Location selected;
	private Scanner in;
	

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int WIDTH = 560;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public ChessCourt(JLabel status, Scanner in) {
        // creates border around the court area, JComponent method
    	this.in = in;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)

        addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(!pieceSelected) {
					Location l = getNearestLoc(e.getX(), e.getY());
					if(l.isValid()) {
						selected = l;
						pieceSelected = true;
					}
				}
				else if(pieceSelected) {
					Location drop = getNearestLoc(e.getX(), e.getY());
					if(selected.isValid() && drop.isValid()) {
					game.makeMove(selected, drop);
					}
					pieceSelected = false;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
        	
        });
        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        game = new ChessGame(in);

        playing = true;
        status.setText("Game In Progress");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    public ChessGame getGame() {
    	return game;
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
        	String playerToMove = "White";
        	if(!game.isWhiteToPlay()) {
        		playerToMove = "Black";
        	}
        	status.setText("Move # "+game.getMoveNum()+". "+playerToMove +" to move.");
            // check for the game end conditions
            if (game.getOutcome() == ChessGame.Outcome.WHITE_WINS) {
                playing = false;
                status.setText("White Wins!");
            }
            else if (game.getOutcome() == ChessGame.Outcome.BLACK_WINS) {
                playing = false;
                status.setText("Black Wins!");
            }
            else if(game.getOutcome() == ChessGame.Outcome.DRAW) {
            	playing = false;
            	status.setText("Draw!");
            }

            // update the display
            repaint();
        }
    }
    
    
    public Location getNearestLoc(int x, int y) {
    	int X = x/Piece.size;
    	int Y = y/Piece.size;
    	return new Location(X, Y);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    	Color original = g.getColor();

        for(int i = 0; i < 8; i++) {
        	for(int j = 0; j < 8; j++) {
        		g.drawRect(i*Piece.size, j*Piece.size, Piece.size, Piece.size);
        		if((i + j) % 2 == 1) {
        			g.setColor(Color.BLUE);
        			g.fillRect(i*Piece.size, j*Piece.size, Piece.size, Piece.size);
        			g.setColor(original);
        		}
        	}
        }
        if(pieceSelected) {
        	g.setColor(Color.RED);
        	g.fillRect(selected.getX()*Piece.size, selected.getY()*Piece.size, Piece.size, Piece.size);
        	g.setColor(original);
        }
        Piece[][] board = game.getBoard();
        for(int i = 0; i < 8; i++) {
        	for(int j = 0; j < 8; j++) {
        		board[i][j].draw(g);
        	}
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, WIDTH);
    }
}