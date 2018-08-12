package parts.game;

public class BoardCords {
    //This is just a data class to hold the tuple for col and row cords on a board in the move list.
    //setting to -1 because 0 is valid.
    public int col = -1;
    public int row = -1;

    public BoardCords(int row, int col) {
        this.col = col;
        this.row = row;
    }
}
