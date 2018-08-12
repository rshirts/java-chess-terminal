package parts.board;

import parts.game.*;
import parts.pieces.ChessPiece;
import parts.pieces.PieceType;

import java.util.*;

// This class is responsible for all the moves and information on the board.
public class ChessBoard {

    public final static int BOARD_SIZE = 8;
    public ChessPiece[][] board;

    // START Data structures

    //used for drawing board.
    String[] charRows = { "8","7","6","5","4","3","2","1" };
    String[] charCols = {"A","B","C","D","E","F","G","H"};

    //used for accessing parts of the board so that it's accessed via the game board indexing system.
    public static Map<Character, Integer> rows = new HashMap<Character, Integer>() {
        {
            put ('8', 0);
            put ('7', 1);
            put ('6', 2);
            put ('5', 3);
            put ('4', 4);
            put ('3', 5);
            put ('2', 6);
            put ('1', 7);
        }
    };
    public static Map<Character, Integer> cols = new HashMap<Character, Integer>() {
        {
            put('A', 0);
            put('B', 1);
            put('C', 2);
            put('D', 3);
            put('E', 4);
            put('F', 5);
            put('G', 6);
            put('H', 7);

        }
    };

    // END Data structures

    public ChessBoard() {
        board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        resetBoard();
    }

    public ChessBoard(ChessBoard chessBoard) {
        board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = chessBoard.getChessPiece(i, j);
            }
        }
    }

    //Deep copy board
    public ChessPiece getChessPiece(int row, int col) {
        ChessPiece returnPiece = board[row][col];
        return returnPiece;
    }

    //This is used when the board needs to be reset to the starting position.
    public void resetBoard() {
        //set black back line
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('A')] = new ChessPiece(PieceType.BLACK_ROOK);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('H')] = new ChessPiece(PieceType.BLACK_ROOK);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('B')] = new ChessPiece(PieceType.BLACK_KNIGHT);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('G')] = new ChessPiece(PieceType.BLACK_KNIGHT);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('C')] = new ChessPiece(PieceType.BLACK_BISHOP);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('F')] = new ChessPiece(PieceType.BLACK_BISHOP);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('D')] = new ChessPiece(PieceType.BLACK_QUEEN);
        board[ChessBoard.rows.get('8')][ChessBoard.cols.get('E')] = new ChessPiece(PieceType.BLACK_KING);
        //set black pawns
        for(int i = 0; i < BOARD_SIZE; i++) {
            board[1][i] = new ChessPiece(PieceType.BLACK_PAWN);
        }
        //set blank spaces
        for(int i = 2; i < 6; i++) {
            for( int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ChessPiece(PieceType.EMPTY);
            }
        }
        //set white pawns
        for(int i = 0; i < BOARD_SIZE; i++) {
            board[6][i] = new ChessPiece(PieceType.WHITE_PAWN);
        }
        //set white black line
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('A')] = new ChessPiece(PieceType.WHITE_ROOK);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('H')] = new ChessPiece(PieceType.WHITE_ROOK);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('B')] = new ChessPiece(PieceType.WHITE_KNIGHT);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('G')] = new ChessPiece(PieceType.WHITE_KNIGHT);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('C')] = new ChessPiece(PieceType.WHITE_BISHOP);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('F')] = new ChessPiece(PieceType.WHITE_BISHOP);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('D')] = new ChessPiece(PieceType.WHITE_QUEEN);
        board[ChessBoard.rows.get('1')][ChessBoard.cols.get('E')] = new ChessPiece(PieceType.WHITE_KING);
    }

    //This will draw the board at the beginning of the game and after each move.
    public void displayBoard() {
        StringBuilder boardString = new StringBuilder();
        for(int i = 0; i < BOARD_SIZE; i++) {
            boardString.append(charRows[i]).append(" | ");
            for(int j = 0; j < BOARD_SIZE; j++) {
                boardString.append(board[i][j].displayChar).append(" ");
            }
            boardString.append("\n");
        }
        boardString.append("   ----------------\n");
        boardString.append("    ");
        for(int i = 0; i < BOARD_SIZE; i++) {
            boardString.append(charCols[i]).append(" ");
        }
        System.out.println(boardString);
    }

    //This is run after a piece moves to make sure it can happen if it can't the move is canceled
    public boolean isMoveLegal(GameMove gameMove, TeamColor teamColor) {
        int fromCol = ChessBoard.cols.get(gameMove.getFromCol());
        int fromRow = ChessBoard.rows.get(gameMove.getFromRow());
        int toCol = ChessBoard.cols.get(gameMove.getToCol());
        int toRow = ChessBoard.rows.get(gameMove.getToRow());
        //These are the modified col and row that are checked to see if the moves inbetween to and from are valid.
        int checkCol = -1;
        int checkRow = -1;
        ChessPiece chessPiece = board[fromRow][fromCol];
        ChessPiece checkThisPiece = null;
        //First we are going to find all valid moves the piece can make
        //Second we will test to see if the toCol and toRow are in that list.
        ArrayList<BoardCords> validMoves = new ArrayList<>();
        //this can be used in a loop to see if more moves are still available
        boolean endOfMove = false;

        if (Debug.TEAM_COLOR == true) {
            //Check to see if right team is moving their piece.
            if (chessPiece.teamColor != teamColor) {
                ChessGame.infoString.append("\nERROR: You can only move pieces from your team.");
                return false;
            }
        }

        //make sure player not trying to move a blank space.
        if(chessPiece.pieceType == PieceType.EMPTY) {
            ChessGame.infoString.append("\nERROR: Cannot move an empty space.");
            return false;
        }

        //region Chess Moves Verification.
        switch (chessPiece.pieceType) {
            //region Pawn Checks
            case BLACK_PAWN:
                //check to see if can move down one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check row boundary and increment
                if(checkRow + 1 < BOARD_SIZE) {
                    checkRow++;
                    checkThisPiece = board[checkRow][checkCol];
                    if(checkThisPiece.pieceType == PieceType.EMPTY) {
                        validMoves.add(new BoardCords(checkRow, checkCol));
                    }
                }

                //check to see if can kill diaginal left one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol -1 >= 0) {
                    checkCol--;
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow++;
                        checkThisPiece = board[checkRow][checkCol];
                        if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check to see if can kill diaginal right one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow++;
                        checkThisPiece = board[checkRow][checkCol];
                        if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check to see if can move down two spaces.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                //This move can only be done if the pawn has not been moved yet.
                if (fromRow == 1) {
                    while(!endOfMove) {
                        //check row boundary and increment
                        if(checkRow + 1 < BOARD_SIZE) {
                            checkRow++;
                            if(checkThisPiece.pieceType == PieceType.EMPTY) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                            }
                            //This needs to be hard coded to see if it can == toRow
                            if(checkRow == fromRow + 2) {
                                endOfMove = true;
                            }
                        }
                    }
                }
                break;

            case WHITE_PAWN:
                //check to see if can move down one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check row boundary and increment
                if(checkRow -1 > 0) {
                    checkRow--;
                    checkThisPiece = board[checkRow][checkCol];
                    if(checkThisPiece.pieceType == PieceType.EMPTY) {
                        validMoves.add(new BoardCords(checkRow, checkCol));
                    }
                }

                //check to see if can kill diaginal left one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol -1 >= 0) {
                    checkCol--;
                    //check row boundary and increment
                    if(checkRow -1 > 0) {
                        checkRow--;
                        checkThisPiece = board[checkRow][checkCol];
                        if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check to see if can kill diaginal right one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    //check row boundary and increment
                    if(checkRow -1 > 0) {
                        checkRow--;
                        checkThisPiece = board[checkRow][checkCol];
                        if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check to see if can move down two spaces.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                //This move can only be done if the pawn has not been moved yet and both spaces are empty.
                if (fromRow == 6) {
                    while(!endOfMove) {
                        //check row boundary and increment
                        if(checkRow -1 >= 0) {
                            checkRow--;
                            if(checkThisPiece.pieceType == PieceType.EMPTY) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                            }
                            //This needs to be hard coded to see if it can == toRow
                            if(checkRow == fromRow - 2) {
                                endOfMove = true;
                            }
                        }
                    }
                }
                break;
            //endregion

            //region King Checks
            case BLACK_KING:
            case WHITE_KING:
                //check diagonal moves
                //check diagonal to see if can move up and right one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    //check row boundary and increment
                    if(checkRow -1 >= 0) {
                        checkRow--;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check diagonal to see if can move right and down one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow++;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check diagonal to see if can move left and down one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol -1 >= 0) {
                    checkCol--;
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow++;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check diagonal to see if can move left and up one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol -1 >= 0) {
                    checkCol--;
                    //check row boundary and increment
                    if(checkRow -1 >= 0) {
                        checkRow--;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check straight moves.
                //check to see if can move up one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check row boundary and increment
                if(checkRow -1 >= 0) {
                    checkRow--;
                    checkThisPiece = board[checkRow][checkCol];
                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                        validMoves.add(new BoardCords(checkRow, checkCol));
                    }
                }

                //check to see if can move right one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check row boundary and increment
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    checkThisPiece = board[checkRow][checkCol];
                    if (checkThisPiece.teamColor != chessPiece.teamColor) {
                        validMoves.add(new BoardCords(checkRow, checkCol));
                    }
                }

                //check to see if can move down one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check row boundary and increment
                if(checkRow + 1 < BOARD_SIZE) {
                    checkRow++;
                    checkThisPiece = board[checkRow][checkCol];
                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                        validMoves.add(new BoardCords(checkRow, checkCol));
                    }
                }

                //check to see if can move left one space.
                checkCol = fromCol;
                checkRow = fromRow;
                //check row boundary and increment
                if(checkCol -1 >= 0) {
                    checkCol--;
                    checkThisPiece = board[checkRow][checkCol];
                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                        validMoves.add(new BoardCords(checkRow, checkCol));
                    }
                }
                break;
            //endregion

            //region Knight Checks
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:

                //check right 1 and up 2.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    //check row boundary and increment
                    if(checkRow - 2 >= 0) {
                        checkRow = checkRow - 2;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check right 1 and down 2.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 1 < BOARD_SIZE) {
                    checkCol++;
                    //check row boundary and increment
                    if(checkRow + 2 < BOARD_SIZE) {
                        checkRow = checkRow + 2;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check right 2 and up  1.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 2 < BOARD_SIZE) {
                    checkCol = checkCol + 2;
                    //check row boundary and increment
                    if(checkRow - 1 >= 0) {
                        checkRow = checkRow - 1;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check right 2 and down  1.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol + 2 < BOARD_SIZE) {
                    checkCol = checkCol + 2;
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow = checkRow + 1;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check left 1 and down 2.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol - 1 >= 0) {
                    checkCol = checkCol - 1;
                    //check row boundary and increment
                    if(checkRow + 2 < BOARD_SIZE) {
                        checkRow = checkRow + 2;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check left 2 and down 1.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol - 2 >= 0) {
                    checkCol = checkCol - 2;
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow = checkRow + 1;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check left 2 and up 1.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol - 2 >= 0) {
                    checkCol = checkCol - 2;
                    //check row boundary and increment
                    if(checkRow - 1 >= 0) {
                        checkRow = checkRow - 1;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }

                //check left 1 and up 2.
                checkCol = fromCol;
                checkRow = fromRow;
                //check col boundary and increment.
                if(checkCol - 1 >= 0) {
                    checkCol = checkCol - 1;
                    //check row boundary and increment
                    if(checkRow - 2 >= 0) {
                        checkRow = checkRow - 2;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is he can't move on top of his own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                        }
                    }
                }
                break;
            //endregion

            //region Queen Checks
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                //check diagonal moves
                //check diagonal to see if can move up and right.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol + 1 < BOARD_SIZE) {
                        checkCol++;
                        //check row boundary and increment
                        if(checkRow - 1 >= 0) {
                            checkRow--;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check diagonal to see if can move down and right.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol + 1 < BOARD_SIZE) {
                        checkCol++;
                        //check row boundary and increment
                        if(checkRow + 1 < BOARD_SIZE) {
                            checkRow++;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check diagonal to see if can move down and left.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol - 1 >= 0) {
                        checkCol--;
                        //check row boundary and increment
                        if(checkRow + 1 < BOARD_SIZE) {
                            checkRow++;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check diagonal to see if can move up and left.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol - 1 >= 0) {
                        checkCol--;
                        //check row boundary and increment
                        if(checkRow - 1 >= 0) {
                            checkRow--;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move up.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkRow - 1 >= 0) {
                        checkRow--;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move down.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow++;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move left.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkCol - 1 >= 0) {
                        checkCol--;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move right.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkCol + 1 < BOARD_SIZE) {
                        checkCol++;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }
                break;
            //endregion

            //region Bishop Moves
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                //check diagonal to see if can move up and right.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol + 1 < BOARD_SIZE) {
                        checkCol++;
                        //check row boundary and increment
                        if(checkRow - 1 >= 0) {
                            checkRow--;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check diagonal to see if can move down and right.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol + 1 < BOARD_SIZE) {
                        checkCol++;
                        //check row boundary and increment
                        if(checkRow + 1 < BOARD_SIZE) {
                            checkRow++;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check diagonal to see if can move down and left.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol - 1 >= 0) {
                        checkCol--;
                        //check row boundary and increment
                        if(checkRow + 1 < BOARD_SIZE) {
                            checkRow++;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check diagonal to see if can move up and left.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check col boundary and increment.
                    if(checkCol - 1 >= 0) {
                        checkCol--;
                        //check row boundary and increment
                        if(checkRow - 1 >= 0) {
                            checkRow--;
                            checkThisPiece = board[checkRow][checkCol];
                            //only restriction is it can't move on top of its own piece
                            if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                validMoves.add(new BoardCords(checkRow, checkCol));
                                //found an enemy piece stop checking.
                                if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                    endOfMove = true;
                                }
                            } else {
                                // Ran into own piece end check.
                                endOfMove = true;
                            }
                        } else {
                            // Ran into the end of board.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }
                break;
            //endregion

            //region Rook Moves
            case BLACK_ROOK:
            case WHITE_ROOK:
                //check to see if can move up.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkRow - 1 >= 0) {
                        checkRow--;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move down.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkRow + 1 < BOARD_SIZE) {
                        checkRow++;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move left.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkCol - 1 >= 0) {
                        checkCol--;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }

                //check to see if can move right.
                checkCol = fromCol;
                checkRow = fromRow;
                endOfMove = false;
                while(!endOfMove) {
                    //check row boundary and increment
                    if(checkCol + 1 < BOARD_SIZE) {
                        checkCol++;
                        checkThisPiece = board[checkRow][checkCol];
                        //only restriction is it can't move on top of its own piece
                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                            validMoves.add(new BoardCords(checkRow, checkCol));
                            //found an enemy piece stop checking.
                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                endOfMove = true;
                            }
                        } else {
                            // Ran into own piece end check.
                            endOfMove = true;
                        }
                    } else {
                        // Ran into the end of board.
                        endOfMove = true;
                    }
                }
                break;
            //endregion
        }
        //endregion

        //Check to see if the toCol, toRow Set is in the list of possible moves.
        for(BoardCords move : validMoves){
            if (move.col == toCol && move.row == toRow) {
                return true;
            }
        }

        //didn't find toCol and toRow to be in the valid move sets return false.
        //The moveString was not valid for that piece.
        ChessGame.infoString.append("\nERROR: That move was not valid.");
        return false;
    }

    //Check for queening
    public void promotePawns() {
        ChessPiece checkPiece = null;
        //check for White pawns to promote.
        for( int i = 0; i < BOARD_SIZE; i++) {
            checkPiece = board[0][i];
            if (checkPiece.pieceType == PieceType.WHITE_PAWN) {
                board[0][i] = new ChessPiece(PieceType.WHITE_QUEEN);
                ChessGame.infoString.append("\nINFO: " + TeamColor.WHITE + " Pawn was promoted.");
            }
        }

        for( int i = 0; i < BOARD_SIZE; i++) {
            checkPiece = board[7][i];
            if (checkPiece.pieceType == PieceType.BLACK_PAWN) {
                board[7][i] = new ChessPiece(PieceType.BLACK_QUEEN);
                ChessGame.infoString.append("\nINFO: " +TeamColor.BLACK + " Pawn was promoted.");
            }
        }
    }

    //This is run after each turn to see if either king is in check and then displayed to player if it's true.
    public boolean checkForCheck() {
        //The two lists used to store each teams moves.
        ArrayList<BoardCords> whiteValidMoves = new ArrayList<>();
        ArrayList<BoardCords> blackValidMoves = new ArrayList<>();
        //The cords for each king.
        BoardCords whiteKing = null;
        BoardCords blackKing = null;
        //this can be used in a loop to see if more moves are still available
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessPiece chessPiece = board[row][col];
                if (chessPiece.teamColor != TeamColor.EMPTY) {
                    //Found black king remember where he is.
                    if (chessPiece.pieceType == PieceType.BLACK_KING) {
                        blackKing = new BoardCords(row, col);
                    }
                    //found White king remember where he is.
                    if (chessPiece.pieceType == PieceType.WHITE_KING) {
                        whiteKing = new BoardCords(row, col);
                    }
                    // Start running everyones moves.
                    int fromCol = col;
                    int fromRow = row;
                    //These are the modified col and row that are checked to see if the moves inbetween to and from are valid.
                    int checkCol = -1;
                    int checkRow = -1;
                    ChessPiece checkThisPiece = null;
                    //First we are going to find all valid moves each team can make
                    //Second we will test to see if the Kings cords are in the moves of the other team.
                    boolean endOfMove = false;

                    //region Chess Moves Verification.
                    switch (chessPiece.pieceType) {
                        //region Pawn Checks
                        case BLACK_PAWN:
                            //check to see if can move down one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check row boundary and increment
                            if(checkRow + 1 < BOARD_SIZE) {
                                checkRow++;
                                checkThisPiece = board[checkRow][checkCol];
                                if(checkThisPiece.pieceType == PieceType.EMPTY) {
                                    blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                }
                            }

                            //check to see if can kill diaginal left one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol -1 >= 0) {
                                checkCol--;
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                                        blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can kill diaginal right one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                                        blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can move down two spaces.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            //This move can only be done if the pawn has not been moved yet.
                            if (fromRow == 1) {
                                while(!endOfMove) {
                                    //check row boundary and increment
                                    if(checkRow + 1 < BOARD_SIZE) {
                                        checkRow++;
                                        if(checkThisPiece.pieceType == PieceType.EMPTY) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //This needs to be hard coded to see if it can == toY
                                        if(checkRow == fromRow + 2) {
                                            endOfMove = true;
                                        }
                                    }
                                }
                            }
                            break;

                        case WHITE_PAWN:
                            //check to see if can move down one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check row boundary and increment
                            if(checkRow -1 > 0) {
                                checkRow--;
                                checkThisPiece = board[checkRow][checkCol];
                                if(checkThisPiece.pieceType == PieceType.EMPTY) {
                                    whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                }
                            }

                            //check to see if can kill diaginal left one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol -1 >= 0) {
                                checkCol--;
                                //check row boundary and increment
                                if(checkRow -1 > 0) {
                                    checkRow--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                                        whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can kill diaginal right one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                //check row boundary and increment
                                if(checkRow -1 > 0) {
                                    checkRow--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    if(checkThisPiece.pieceType != PieceType.EMPTY && checkThisPiece.teamColor != chessPiece.teamColor) {
                                        whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can move down two spaces.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            //This move can only be done if the pawn has not been moved yet and both spaces are empty.
                            if (fromRow == 6) {
                                while(!endOfMove) {
                                    //check row boundary and increment
                                    if(checkRow -1 >= 0) {
                                        checkRow--;
                                        if(checkThisPiece.pieceType == PieceType.EMPTY) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //This needs to be hard coded to see if it can == toY
                                        if(checkRow == fromRow - 2) {
                                            endOfMove = true;
                                        }
                                    }
                                }
                            }
                            break;
                        //endregion

                        //region King Checks
                        case BLACK_KING:
                        case WHITE_KING:
                            //check diagonal moves
                            //check diagonal to see if can move up and right one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                //check row boundary and increment
                                if(checkRow -1 >= 0) {
                                    checkRow--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check diagonal to see if can move right and down one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check diagonal to see if can move left and down one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol -1 >= 0) {
                                checkCol--;
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check diagonal to see if can move left and up one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol -1 >= 0) {
                                checkCol--;
                                //check row boundary and increment
                                if(checkRow -1 >= 0) {
                                    checkRow--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check straight moves.
                            //check to see if can move up one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check row boundary and increment
                            if(checkRow -1 >= 0) {
                                checkRow--;
                                checkThisPiece = board[checkRow][checkCol];
                                if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                    if(chessPiece.teamColor == TeamColor.BLACK) {
                                        blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                    if(chessPiece.teamColor == TeamColor.WHITE) {
                                        whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can move right one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check row boundary and increment
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                checkThisPiece = board[checkRow][checkCol];
                                if (checkThisPiece.teamColor != chessPiece.teamColor) {
                                    if(chessPiece.teamColor == TeamColor.BLACK) {
                                        blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                    if(chessPiece.teamColor == TeamColor.WHITE) {
                                        whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can move down one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check row boundary and increment
                            if(checkRow + 1 < BOARD_SIZE) {
                                checkRow++;
                                checkThisPiece = board[checkRow][checkCol];
                                if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                    if(chessPiece.teamColor == TeamColor.BLACK) {
                                        blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                    if(chessPiece.teamColor == TeamColor.WHITE) {
                                        whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }

                            //check to see if can move left one space.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check row boundary and increment
                            if(checkCol -1 >= 0) {
                                checkCol--;
                                checkThisPiece = board[checkRow][checkCol];
                                if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                    if(chessPiece.teamColor == TeamColor.BLACK) {
                                        blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                    if(chessPiece.teamColor == TeamColor.WHITE) {
                                        whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                    }
                                }
                            }
                            break;
                        //endregion

                        //region Knight Checks
                        case BLACK_KNIGHT:
                        case WHITE_KNIGHT:

                            //check right 1 and up 2.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                //check row boundary and increment
                                if(checkRow - 2 >= 0) {
                                    checkRow = checkRow - 2;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check right 1 and down 2.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 1 < BOARD_SIZE) {
                                checkCol++;
                                //check row boundary and increment
                                if(checkRow + 2 < BOARD_SIZE) {
                                    checkRow = checkRow + 2;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check right 2 and up  1.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 2 < BOARD_SIZE) {
                                checkCol = checkCol + 2;
                                //check row boundary and increment
                                if(checkRow - 1 >= 0) {
                                    checkRow = checkRow - 1;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check right 2 and down  1.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol + 2 < BOARD_SIZE) {
                                checkCol = checkCol + 2;
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow = checkRow + 1;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check left 1 and down 2.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol - 1 >= 0) {
                                checkCol = checkCol - 1;
                                //check row boundary and increment
                                if(checkRow + 2 < BOARD_SIZE) {
                                    checkRow = checkRow + 2;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check left 2 and down 1.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol - 2 >= 0) {
                                checkCol = checkCol - 2;
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow = checkRow + 1;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check left 2 and up 1.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol - 2 >= 0) {
                                checkCol = checkCol - 2;
                                //check row boundary and increment
                                if(checkRow - 1 >= 0) {
                                    checkRow = checkRow - 1;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }

                            //check left 1 and up 2.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            //check col boundary and increment.
                            if(checkCol - 1 >= 0) {
                                checkCol = checkCol - 1;
                                //check row boundary and increment
                                if(checkRow - 2 >= 0) {
                                    checkRow = checkRow - 2;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is he can't move on top of his own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                    }
                                }
                            }
                            break;
                        //endregion

                        //region Queen Checks
                        case BLACK_QUEEN:
                        case WHITE_QUEEN:
                            //check diagonal moves
                            //check diagonal to see if can move up and right.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol + 1 < BOARD_SIZE) {
                                    checkCol++;
                                    //check row boundary and increment
                                    if(checkRow - 1 >= 0) {
                                        checkRow--;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check diagonal to see if can move down and right.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol + 1 < BOARD_SIZE) {
                                    checkCol++;
                                    //check row boundary and increment
                                    if(checkRow + 1 < BOARD_SIZE) {
                                        checkRow++;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check diagonal to see if can move down and left.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol - 1 >= 0) {
                                    checkCol--;
                                    //check row boundary and increment
                                    if(checkRow + 1 < BOARD_SIZE) {
                                        checkRow++;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check diagonal to see if can move up and left.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol - 1 >= 0) {
                                    checkCol--;
                                    //check row boundary and increment
                                    if(checkRow - 1 >= 0) {
                                        checkRow--;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move up.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkRow - 1 >= 0) {
                                    checkRow--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move down.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move left.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkCol - 1 >= 0) {
                                    checkCol--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move right.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkCol + 1 < BOARD_SIZE) {
                                    checkCol++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }
                            break;
                        //endregion

                        //region Bishop Moves
                        case BLACK_BISHOP:
                        case WHITE_BISHOP:
                            //check diagonal to see if can move up and right.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol + 1 < BOARD_SIZE) {
                                    checkCol++;
                                    //check row boundary and increment
                                    if(checkRow - 1 >= 0) {
                                        checkRow--;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check diagonal to see if can move down and right.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol + 1 < BOARD_SIZE) {
                                    checkCol++;
                                    //check row boundary and increment
                                    if(checkRow + 1 < BOARD_SIZE) {
                                        checkRow++;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check diagonal to see if can move down and left.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol - 1 >= 0) {
                                    checkCol--;
                                    //check row boundary and increment
                                    if(checkRow + 1 < BOARD_SIZE) {
                                        checkRow++;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check diagonal to see if can move up and left.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check col boundary and increment.
                                if(checkCol - 1 >= 0) {
                                    checkCol--;
                                    //check row boundary and increment
                                    if(checkRow - 1 >= 0) {
                                        checkRow--;
                                        checkThisPiece = board[checkRow][checkCol];
                                        //only restriction is it can't move on top of its own piece
                                        if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                            if(chessPiece.teamColor == TeamColor.BLACK) {
                                                blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            if(chessPiece.teamColor == TeamColor.WHITE) {
                                                whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                            }
                                            //found an enemy piece stop checking.
                                            if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                                endOfMove = true;
                                            }
                                        } else {
                                            // Ran into own piece end check.
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into the end of board.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }
                            break;
                        //endregion

                        //region Rook Moves
                        case BLACK_ROOK:
                        case WHITE_ROOK:
                            //check to see if can move up.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkRow - 1 >= 0) {
                                    checkRow--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move down.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkRow + 1 < BOARD_SIZE) {
                                    checkRow++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move left.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkCol - 1 >= 0) {
                                    checkCol--;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }

                            //check to see if can move right.
                            checkCol = fromCol;
                            checkRow = fromRow;
                            endOfMove = false;
                            while(!endOfMove) {
                                //check row boundary and increment
                                if(checkCol + 1 < BOARD_SIZE) {
                                    checkCol++;
                                    checkThisPiece = board[checkRow][checkCol];
                                    //only restriction is it can't move on top of its own piece
                                    if(checkThisPiece.teamColor != chessPiece.teamColor) {
                                        if(chessPiece.teamColor == TeamColor.BLACK) {
                                            blackValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        if(chessPiece.teamColor == TeamColor.WHITE) {
                                            whiteValidMoves.add(new BoardCords(checkRow, checkCol));
                                        }
                                        //found an enemy piece stop checking.
                                        if(checkThisPiece.teamColor != TeamColor.EMPTY) {
                                            endOfMove = true;
                                        }
                                    } else {
                                        // Ran into own piece end check.
                                        endOfMove = true;
                                    }
                                } else {
                                    // Ran into the end of board.
                                    endOfMove = true;
                                }
                            }
                            break;
                        //endregion
                    }
                    //endregion
                }
            }
        }

        //If the kings are not found then the game is over.
        //These need to be displayed now because menu and ChessGame.infoString will not be displayed again.
        if(whiteKing == null) {
            System.out.println("White King is dead! ***Black has won!*** ");
            return false;
        }

        if(blackKing == null) {
            System.out.println("Black King is dead! ***White has won!*** ");
            return false;
        }

        //Check to see if the white king is listed in possible Black team moves.
        for(BoardCords move : blackValidMoves){
            if (move.col == whiteKing.col && move.row == whiteKing.row) {
                ChessGame.infoString.append("\nWARNING: The White King is in CHECK.");
            }
        }

        //Check to see if the black King is listed in the possible White team moves.
        for(BoardCords move : whiteValidMoves){
            if (move.col == blackKing.col && move.row == blackKing.row) {
                ChessGame.infoString.append("\nWARNING: The Black King is in CHECK.");
            }
        }

        return true;
    }
}
