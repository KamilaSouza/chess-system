package chess;

import boardgame.Position;

public class ChessPosition {
    private char column;
    private int line;

    public ChessPosition(char column, int line) {
        if (column < 'a' || column > 'h' || line < 1 || line > 8) {
            throw new ChessException("Error instantiating ChessPosition. Must be between a1 and h8.");
        }
        this.column = column;
        this.line = line;
    }

    public char getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    protected Position toPosition() { //ChessPosition to position
        return new Position(8 - line, column - 'a'); // 'a' - 'a' = 0 , 'b' - 'a' = 1 ...
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('a' + position.getColumn()), 8 - position.getLine());
    }

    @Override
    public String toString() {
        return "" + column + line; // "" to concatenate strings
    }
}
