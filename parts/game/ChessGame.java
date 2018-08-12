package parts.game;

import parts.board.ChessBoard;
import parts.pieces.ChessPiece;
import parts.pieces.PieceType;

import java.util.Scanner;

//This class is responsible for managing the flow of the game.
public class ChessGame {
    //set this to false if you want to remove move validation.
    //This is used to move the king around to check for Check quickly.
    //Or to move a pawn to check for Queening.

    //This is the board before the turn has been processed
    ChessBoard rawBoard;
    //This is the board after the turn has been processed
    //This will be used more for when AI is enabled to keep the previous state of the board.
    //For Min / Max.
    ChessBoard turnBoard;
    //Current moveString to process.
    GameMove currentMove;

    String moveString = null;
    Scanner scanner = new Scanner(System.in);

    //This is where error messages and warning should be displayed.
    public static StringBuilder infoString = new StringBuilder();

    TeamColor teamColor = TeamColor.WHITE;
    boolean firstMove = true;

    public ChessGame(ChessBoard chessBoard) {
        rawBoard = chessBoard;
    }

    public void startGame() {
        do {
            //show the rawboard.
            rawBoard.displayBoard();
            if (moveString == null || !moveString.equals("Q")) {
                //Display infostring and then reset it.
                System.out.println(infoString);
                infoString.setLength(0);
                //Only display this the first time.
                if (firstMove) {
                    System.out.println("Enter q to quit game.");
                    System.out.println("Example Move: D2 D3");
                    firstMove = false;
                }
                System.out.print(teamColor + "'s move: ");
                moveString = scanner.nextLine().trim().toUpperCase();
                //check to see if game is quiting.
                if (moveString.equals("DEBUG")){
                    debugMenu();
                }
                else if (!moveString.equals("Q")) {
                    if (doMove(moveString)) {
                        //Check to see if a pawn needs promoting.
                        rawBoard.promotePawns();
                        //Check to see if one of the kings is now in check.
                        if(!rawBoard.checkForCheck()) {
                            System.out.println("Game is over, Thanks for playing.");
                            moveString = "Q";
                        }
                    }
                }
            }
        } while (!moveString.equals("Q"));
    }

    // This is used for toggling the debug options.
    private void debugMenu() {
        do {
            System.out.println("VALIDATE_MOVE (true checks for valid moves) = " + Debug.VALID_MOVE);
            System.out.println("TEAM_COLOR (makers sure proper turns happen) = " + Debug.TEAM_COLOR);
            System.out.println("If VALIDATE_MOVE = false TEAM_COLOR wont be checked. ");
            System.out.println("Enter 1 to toggle VALIDATE_MOVE.");
            System.out.println("Enter 2 to toggle TEAM_COLOR.");
            System.out.print("Enter Q to exit Menu: ");
            moveString = scanner.nextLine().trim().toUpperCase();
            if (moveString.equals("1")) {
                Debug.VALID_MOVE = !Debug.VALID_MOVE;
                System.out.println("VALID_MOVE changed.");
            } else if (moveString.equals("2")) {
                Debug.TEAM_COLOR = !Debug.TEAM_COLOR;
                System.out.println("TEAM_COLOR changed.");
            } else if (moveString.equals("Q")) {
                System.out.println("Exiting Debug.");
            } else {
                System.out.println("Unrecognized Command.");
            }
        } while (!moveString.equals("Q"));
        //need to set moveString back to null for main menu.
        moveString = "";
    }

    //main should check for true to see if it needs to re-prompt for a new moveString for a new player.
    public boolean doMove(String moveString) {
        //need to reset the move each time incase there was an error on previous move.
        currentMove = null;
        currentMove = new GameMove(moveString);
        if (!currentMove.isValidMove()) {
            //The moveString was invalid do not process moveString.
            ChessGame.infoString.append("\nERROR: Invalid move command entered.");
            return false;
        }

        turnBoard = new ChessBoard(rawBoard);

        if (Debug.VALID_MOVE == true){
            if(!turnBoard.isMoveLegal(currentMove, teamColor)){
                return false;
            }
        }

        //Do the chess move.
        //extracting out the chesspiece so we can get it's color for debug mode.
        ChessPiece movingPiece = turnBoard.board[ChessBoard.rows.get(currentMove.getFromRow())][ChessBoard.cols.get(currentMove.getFromCol())];
        turnBoard.board[ChessBoard.rows.get(currentMove.getToRow())][ChessBoard.cols.get(currentMove.getToCol())] = movingPiece;

        //put a blank space the previous piece was.
        turnBoard.board[ChessBoard.rows.get(currentMove.getFromRow())][ChessBoard.cols.get(currentMove.getFromCol())]
                = new ChessPiece(PieceType.EMPTY);

        //switch to other players turn.
        if (Debug.TEAM_COLOR == true) {
            if (teamColor == TeamColor.WHITE) {
                teamColor = TeamColor.BLACK;
            } else {
                teamColor = TeamColor.WHITE;
            }
        } else {
            //If we turned off player turn just always keep it that players turn color.
            teamColor = movingPiece.teamColor;
        }

        rawBoard = turnBoard;
        return true;
    }
}
