package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getLines()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        //                  x
        //                  x
        //              x x R x x
        //                  x
        //                  x

        //above
        p.setValues(position.getLine() - 1, position.getColumn());
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
            p.setLine(p.getLine() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //left
        p.setValues(position.getLine(), position.getColumn() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
            p.setColumn(p.getColumn() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //right
        p.setValues(position.getLine(), position.getColumn() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
            p.setColumn(p.getColumn() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //below
        p.setValues(position.getLine() + 1, position.getColumn());
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
            p.setLine(p.getLine() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        return mat; // all positions are false

    }
}
