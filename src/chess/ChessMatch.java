package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
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

    public boolean[][] possibleMoves(ChessPosition originPosition) {
        Position position = originPosition.toPosition();
        validateOriginPosition(position);
        return board.piece(position).possibleMoves();

    }

    public ChessPiece performChessMove(ChessPosition originPosition, ChessPosition targetPosition) {
        Position origin = originPosition.toPosition();
        Position target = targetPosition.toPosition();
        validateOriginPosition(origin);
        validateTargetPosition(origin, target);
        Piece capturedPiece = makeMove(origin, target);
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    private void validateOriginPosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece in origin position.");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours.");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
    }

    private void validateTargetPosition(Position origin, Position target) {
        if (!board.piece(origin).possibleMove(target)) {
            throw new ChessException("The chosen piece can not move to target position.");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE; //ternary operator
    }

    private Piece makeMove(Position origin, Position target) {
        Piece p = board.removePiece(origin);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        return capturedPiece;
    }

    private void placeNewPiece(char column, int line, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, line).toPosition());
    }

    private void initialSetup() {
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }
}
