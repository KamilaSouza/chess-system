package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "P";
    }

    //                x                         y x y
    // First        y x y         Other           P
    // move           P           moves

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getLines()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        if (getColor() == Color.WHITE) {
            p.setValues(position.getLine() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getLine()][p.getColumn()] = true;
            }
            p.setValues(position.getLine() - 2, position.getColumn());
            Position p2 = new Position(position.getLine() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getLine()][p.getColumn()] = true;
            }
            p.setValues(position.getLine() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getLine()][p.getColumn()] = true;
            }
            p.setValues(position.getLine() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getLine()][p.getColumn()] = true;
            }

            // Special move en passant
            if (position.getLine() == 3) {
                Position left = new Position(position.getLine(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassant()) {
                    mat[left.getLine() - 1][left.getColumn()] = true;
                }
                Position right = new Position(position.getLine(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassant()) {
                    mat[right.getLine() - 1][right.getColumn()] = true;
                }
            }

        } else {
            p.setValues(position.getLine() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getLine()][p.getColumn()] = true;
            }
            p.setValues(position.getLine() + 2, position.getColumn());
            Position p2 = new Position(position.getLine() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getLine()][p.getColumn()] = true;
            }
            p.setValues(position.getLine() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getLine()][p.getColumn()] = true;
            }
            p.setValues(position.getLine() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getLine()][p.getColumn()] = true;
            }

            // Special move en passant
            if (position.getLine() == 4) {
                Position left = new Position(position.getLine(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassant()) {
                    mat[left.getLine() + 1][left.getColumn()] = true;
                }
                Position right = new Position(position.getLine(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassant()) {
                    mat[right.getLine() + 1][right.getColumn()] = true;
                }
            }
        }
        return mat;
    }

}
