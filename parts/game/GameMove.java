package parts.game;

//This class is responsible for creating moves and making sure submited moves are within the board range.
//and converting the input recieved to a move that works better for a two dimensional array.
public class GameMove {

    private char fromRow = ' ';
    private char fromCol = ' ';
    private char toRow = ' ';
    private char toCol = ' ';
    private boolean validMove = false;

    public GameMove(String moveString) {
        //split out the to and from moves.
        //Check for proper move pattern
        if(moveString.matches("[A-H][1-8]\\s+[A-H][1-8]")){
            String[] splitString = moveString.trim().split("\\s+");
            //break out each row and column for each moveString.
            char[] fromCords = splitString[0].toCharArray();
            char[] toCords = splitString[1].toCharArray();
            fromRow = fromCords[1];
            fromCol = fromCords[0];
            toRow = toCords[1];
            toCol = toCords[0];
            validMove = true;
        }
    }

    public char getFromRow() {
        return fromRow;
    }

    public char getFromCol() {
        return fromCol;
    }

    public char getToRow() {
        return toRow;
    }

    public char getToCol() {
        return toCol;
    }

    public boolean isValidMove() {
        return validMove;
    }
}
