package application;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;

public class Main {

    public static void main(String[] args) {
        // write your code here

        ChessMatch chessMatch = new ChessMatch();
        UI.printBoard((chessMatch.getPieces()));

    }
}
