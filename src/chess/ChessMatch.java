package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getLines()][board.getColumns()];
        for (int i = 0; i < board.getLines(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j); //DOWNCASTING
            }
        }
        return mat;
    }

    private void placeNewPiece(char column, int line, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, line).toPosition());
    }

    private void initialSetup() {
        placeNewPiece('b', 6, new Rook(board, Color.WHITE));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
    }
}
