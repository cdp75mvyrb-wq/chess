package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.util.List;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    public static String draw(ChessBoard board, String color) {
        String boardString = "";
        List<String> whiteLabels = List.of(EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY + RESET_BG_COLOR);
        List<String> blackLabels = List.of(EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY + RESET_BG_COLOR);
        boardString += SET_BG_COLOR_DARK_GREY;
        boolean white = color == "WHITE";
        List<String> letterLabels = white ? whiteLabels : blackLabels;
        boardString =drawLetterLabels(boardString, letterLabels);
        return boardString;
    }

    private static String drawLetterLabels(String board, List<String> labels) {
        for (String label : labels) {
            board += label;
        }
        return board;
    }
}
