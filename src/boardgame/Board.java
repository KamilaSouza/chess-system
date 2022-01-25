package boardgame;

public class Board {

    private int lines;
    private int columns;
    private Piece [][] pieces; // matrix of pieces

    public Board(int lines, int columns) {
        this.lines = lines;
        this.columns = columns;
        pieces = new Piece[lines][columns];
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
