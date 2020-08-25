=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: bqqian
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays: I used an 8x8 2-D array of Piece objects to store the locations of the pieces. After each move made,
 the 2-D array was updated accordingly. 

  2. File I/O: I included both a "save position to file" and a "load position from file" function to my game. Saved positions 
  are formatted as a single line containing a "W" or "B" (indicating whether it is white to move or black to move), followed by
  an 8x8, tab-delimited array, where white pieces are denoted by 'K','Q','R','B','N','P', and black pieces are denoted by 'k','q','r','n','p', and empty
  squares are denoted as '-'s. When loading a position from a file, any time a file not of this format is loaded, an "invalid file format" message is given.
  This is different from what I included in my original project proposal, in which I originally planned to store games in algebraic move notation. I feel that
  the file format I am now using makes more sense and results in less complications while parsing.
  
  3. Interface/Subtyping: I created an abstract class called 'Piece', which was a supertype of the King, Queen, Rook, Bishop, Knight, Pawn, and EmptyPiece classes.
  The subclasses mainly differ in their respective implementations of the getMoves function, which returns a TreeSet of legal moves for the piece when given a board
  position. In the project proposal, I originally planned on using an interface, but I eventually found that an abstract class would make more sense as there were some method
  bodies that I wanted the superclass to contain.  
  
  4. Complex game logic: In addition to standard piece movement, I implemented en passant, castle, check, preventing illegal moves, pawn promotion, 
   checkmate, draw by stalemate, draw by three-fold repetition, and draw by the 50-move rule. In addition, I implemented the "no castling in/through
   check" rule. 


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Piece: 
- An abstract class, superclass of each of the classes for the different types of pieces
- Stores whether the piece is white/black, whether it has moved yet (relevant to some pieces in the game), 
the image of the piece (relevant for graphics), a method with body for filtering out moves that result in check 
(relevant for preventing illegal moves, and necessary for all pieces), and a method for drawing the image of the piece 
given a Graphics object. In addition, it contains abstract methods such as getMoves (returns a TreeSet 
of legal moves, implemented differently by each piece) and getClone (clones the given piece, relevant for setting up 
hypothetical board positions to determine whether a move is illegal or not). Lastly, it contains the abstract method
getCharacter, which is implemented differently by each piece, and is useful for writing board positions to file.   

Pawn, King, Queen, Bishop, Rook, Knight:
- Subclasses of Piece which each implement getMoves differently. Some pieces have other features (e.g: pawns need a field
to store whether they can be en-passanted or not)

Location:
- A class used for convenience of storing positions on the board. I used this because functions such as getMove return a Set of
legal move coordinates, so a coordinate-like object was necessary. I also used this class to track whether a given coordinate
was given as a result of a legal vertical pawn move. This was necessary because vertical pawn moves are the only exception to the rule that
pieces can capture wherever their legal moves are.

ChessGame:
- The class that tracks the status of the game. It contains the 2D array representing the chess board, tracks whose move it is
and the move number, handles the save and load functions, handles piece movement, and checks the status of the board after each
move made (if it's checkmate/stalemate or still in progress). In addition, it has some functions for the state of a given input board
(whether there is stalemate/checkmate/check on the board).

ChessCourt:
- Very similar to the GameCourt class in the MushroomGame files. Handles MouseEvents for selecting and moving pieces, and updates the board
state.

Game:
- Analogous to the Game class in the MushroomGame files. Handles graphics.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  There were many. There are many special moves in chess that work under very specific conditions (castling, en passant,
  pawn promotion, etc.). This required me to handle these specific cases in the makeMove method of the ChessGame class. 
  In addition, the "draw by 3-fold repetition" rule, which states that if the same position ever appears on the board 3 times
  over the course of the game, the game is drawn, was difficult to implement. The natural idea is to store the board states at 
  each move over the course of a game in a collection, and to analyze that collection after each move. However, I thought that 
  this would be pretty inefficient. I instead came up with the idea of creating a hashcode function for a given board position,
  and instead stored the hashcodes in a collection instead. Comparing hashcodes was sufficient to determine whether a position 
  had appeared 3 times already. 


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think the class hierarchy with the Piece class and its subclasses was well-designed. I am somewhat dissatisfied with how the 
Location objects sometimes feel redundant, as each piece is tracked by their Location object as well as their position on the 2D
array. However, I couldn't think of a way of using only one or the other, as the Location objects are necessary for returning 
coordinates of possible legal moves, whereas the 2D array is necessary for determining what those possible legal moves are. 
In addition, many functions in the Piece class required a Piece[][] object as a parameter, which I felt to be cumbersome. However, I am 
unsure whether there's a better way of handling this as well. I originally planned on having the Piece class store a Piece[][] object 
to represent the board that the piece is on, but then realized that this wouldn't be ideal as many states in chess depend on whether 
hypothetical board positions are valid, rather than the current board position. 


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  Images:
  https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
