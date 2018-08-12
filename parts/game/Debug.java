package parts.game;

//This class just holds the constants for checking for debug modes
public class Debug {
    //True makes sure the move is valid (pawns cant teleport across the board)
    //If this is false then there will be no check for TEAM_COLOR either.
    public static boolean VALID_MOVE = true;
    //true will check for proper turns
    public static boolean TEAM_COLOR = true;

}
