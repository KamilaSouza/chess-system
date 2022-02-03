package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check; // starts at false by default
    private boolean checkMate;
    private ChessPiece enPassant;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

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

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassant() {
        return enPassant;
    }

    public ChessPiece getPromoted() {
        return promoted;
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

        if (testCheck(currentPlayer)) {
            undoMove(origin, target, capturedPiece);
            throw new ChessException("You can't put yourself in check.");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // Special move Promotion - needs to be before check
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getLine() == 0) || (movedPiece.getColor() == Color.BLACK && target.getLine() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }


        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // Special move en passant
        if (movedPiece instanceof Pawn && (target.getLine() == origin.getLine() - 2 || target.getLine() == origin.getLine() + 2)) {
            enPassant = movedPiece;
        } else {
            enPassant = null;
        }
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

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted.");
        }
        if (!type.equals("B") && !type.equals("H") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }
        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("H")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }

    private Piece makeMove(Position origin, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(origin);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // Special move KingSide rook
        if (p instanceof King && target.getColumn() == origin.getColumn() + 2) {
            Position originT = new Position(origin.getLine(), origin.getColumn() + 3);
            Position targetT = new Position(origin.getLine(), origin.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(originT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // Special move QueenSide rook
        if (p instanceof King && target.getColumn() == origin.getColumn() - 2) {
            Position originT = new Position(origin.getLine(), origin.getColumn() - 4);
            Position targetT = new Position(origin.getLine(), origin.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(originT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // Special move en passant
        if (p instanceof Pawn) {
            if (origin.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getLine() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getLine() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position origin, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, origin);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // Special move KingSide rook
        if (p instanceof King && target.getColumn() == origin.getColumn() + 2) {
            Position originT = new Position(origin.getLine(), origin.getColumn() + 3);
            Position targetT = new Position(origin.getLine(), origin.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, originT);
            rook.decreaseMoveCount();
        }

        // Special move QueenSide rook
        if (p instanceof King && target.getColumn() == origin.getColumn() - 2) {
            Position originT = new Position(origin.getLine(), origin.getColumn() - 4);
            Position targetT = new Position(origin.getLine(), origin.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, originT);
            rook.decreaseMoveCount();
        }

        // Special move en passant
        if (p instanceof Pawn) {
            if (origin.getColumn() != target.getColumn() && capturedPiece == enPassant) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }

                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE; // if color is white, return black, else return white
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + "King on the board"); // should not happen
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getLine()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getLines(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position origin = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(origin, target);
                        boolean testCheck = testCheck(color);
                        undoMove(origin, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int line, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, line).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
