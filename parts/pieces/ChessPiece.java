package parts.pieces;

import parts.game.TeamColor;

public class ChessPiece {

    //what piece is this.
    public PieceType pieceType = null;
    //what does this piece look like.
    public char displayChar = ' ';
    //This is just a check to see what color a piece is. Since intellij doesn't support custom enum values.
    public TeamColor teamColor = null;

    public ChessPiece(PieceType pieceType) {
        this.pieceType = pieceType;
        switch (pieceType) {
            case BLACK_KING:
                displayChar = 'k';
                teamColor = TeamColor.BLACK;
                break;
            case BLACK_QUEEN:
                displayChar = 'q';
                teamColor = TeamColor.BLACK;
                break;
            case BLACK_ROOK:
                displayChar = 'r';
                teamColor = TeamColor.BLACK;
                break;
            case BLACK_KNIGHT:
                displayChar = 'n';
                teamColor = TeamColor.BLACK;
                break;
            case BLACK_BISHOP:
                displayChar = 'b';
                teamColor = TeamColor.BLACK;
                break;
            case BLACK_PAWN:
                displayChar = 'p';
                teamColor = TeamColor.BLACK;
                break;
            case EMPTY:
                displayChar = '.';
                teamColor = TeamColor.EMPTY;
                break;
            case WHITE_PAWN:
                displayChar = 'P';
                teamColor = TeamColor.WHITE;
                break;
            case WHITE_BISHOP:
                displayChar = 'B';
                teamColor = TeamColor.WHITE;
                break;
            case WHITE_KNIGHT:
                displayChar = 'N';
                teamColor = TeamColor.WHITE;
                break;
            case WHITE_ROOK:
                displayChar = 'R';
                teamColor = TeamColor.WHITE;
                break;
            case WHITE_QUEEN:
                displayChar = 'Q';
                teamColor = TeamColor.WHITE;
                break;
            case WHITE_KING:
                displayChar = 'K';
                teamColor = TeamColor.WHITE;
                break;
        }
    }
}
