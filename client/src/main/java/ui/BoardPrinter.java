package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.util.List;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    private String squareColor = SET_BG_COLOR_LIGHT_GREY;

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective) {
        StringBuilder out = new StringBuilder();
        out.append("\n");
        List<String> whiteColumns = List.of(EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY);
        List<String> blackColumns = List.of(EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY);
        List<Integer> whiteRows = List.of(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> blackRows = whiteRows.reversed();
        boolean white = perspective == ChessGame.TeamColor.WHITE;
        List<Integer> rows = white ? whiteRows : blackRows;
        List<String> cols = white ? whiteColumns : blackColumns;
        drawLetterLabels(out, cols);
        drawRows(board, out, rows);
        drawLetterLabels(out, cols);
        return out.toString();
    }

    private static void drawRows(ChessBoard board, StringBuilder out, List<Integer> rowNumbers) {
        for (Integer row : rowNumbers) {
            out.append(SET_BG_COLOR_BLACK + " " + row + " " + RESET_BG_COLOR);
            for (Integer col : rowNumbers) {
                out.append(((row + col) % 2) == 1 ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY);
                out.append(" " + "+" + " ");
            }
            out.append(SET_BG_COLOR_BLACK + " " + row + " " + RESET_BG_COLOR + "\n");
        }
    }

    private static void drawLetterLabels(StringBuilder out, List<String> labels) {
        for (String label : labels) {
            out.append(label);
        }
        out.append("\n");
    }
}
