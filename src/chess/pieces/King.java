package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    private ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
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

        //Special move castling

        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            //KingSide rook
            Position posT1 = new Position(position.getLine(), position.getColumn() + 3);
            if (testRookCastling(posT1)) {
                Position p1 = new Position(position.getLine(), position.getColumn() + 1);
                Position p2 = new Position(position.getLine(), position.getColumn() + 2);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getLine()][position.getColumn() + 2] = true;
                }

            }

            //QueenSide rook
            Position posT2 = new Position(position.getLine(), position.getColumn() - 4);
            if (testRookCastling(posT2)) {
                Position p1 = new Position(position.getLine(), position.getColumn() - 1);
                Position p2 = new Position(position.getLine(), position.getColumn() - 2);
                Position p3 = new Position(position.getLine(), position.getColumn() - 3);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
                    mat[position.getLine()][position.getColumn() - 2] = true;
                }

            }
        }

        return mat; // all positions are false
    }
}
