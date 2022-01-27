package boardgame;

public class Board {

    private int lines;
    private int columns;
    private Piece[][] pieces; // matrix of pieces

    public Board(int lines, int columns) {
        if (lines < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 line and 1 column");
        }
        this.lines = lines;
        this.columns = columns;
        pieces = new Piece[lines][columns];
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int line, int column) {
        if (!positionExists(line, column)) {
            throw new BoardException("Position not on the board");
        }
        return pieces[line][column];
    }

    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        return pieces[position.getLine()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)){
            throw new BoardException("There is already a piece on position "+ position);
        }
        pieces[position.getLine()][position.getColumn()] = piece;  //the position in the board receives the piece
        piece.position = position;
    }

    private boolean positionExists(int line, int column) {
        return line >= 0 && line < lines && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getLine(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {  // I need to know if the position exists before look for the piece
            throw new BoardException("Position not on the board");
        }
        return piece(position) != null;
    }
}
