package boardgame;

public class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null; //the piece is not in the board yet
    }

    protected Board getBoard() {
        return board;
    }

}
