import parts.board.ChessBoard;
import parts.game.ChessGame;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Get a new board.
        ChessBoard mainBoard = new ChessBoard();
        //Start a game.
        ChessGame chessGame = new ChessGame(mainBoard);
        chessGame.startGame();
    }
}
