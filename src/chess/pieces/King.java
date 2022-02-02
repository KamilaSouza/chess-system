package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getLines()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        //            x x x
        //            x K x
        //            x x x

        //above
        p.setValues(position.getLine() - 1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //below
        p.setValues(position.getLine() + 1, position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //left
        p.setValues(position.getLine(), position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //right
        p.setValues(position.getLine(), position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //north west
        p.setValues(position.getLine() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //north east
        p.setValues(position.getLine() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //south west
        p.setValues(position.getLine() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }

        //south east
        p.setValues(position.getLine() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getLine()][p.getColumn()] = true;
        }


        return mat; // all positions are false
    }
}
