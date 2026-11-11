package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.List;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    private static List<String> boardColors = List.of(SET_BG_COLOR_WALNUT, SET_BG_COLOR_SAGE);

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective) {
        StringBuilder out = new StringBuilder();
        out.append("\n");
        List<String> whiteColumns = List.of(EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY);
        List<String> blackColumns = List.of(EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY);
        List<Integer> whiteRows = List.of(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> blackRows = whiteRows.reversed();
        boolean white = perspective == WHITE;
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
            for (Integer col : rowNumbers.reversed()) {
                out.append(((row + col) % 2) == 0 ? boardColors.get(0) : boardColors.get(1));
                out.append(getPiece(board, row, col));
            }
            out.append(SET_BG_COLOR_BLACK + " " + row + " " + RESET_BG_COLOR + "\n");
        }
    }

    private static String getPiece(ChessBoard board, Integer row, Integer col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {return EMPTY;}
        else {
            ChessGame.TeamColor color = piece.getTeamColor();
            ChessPiece.PieceType type = piece.getPieceType();
            String textColor = (color == WHITE) ? SET_TEXT_COLOR_IVORY : SET_TEXT_COLOR_BLACK;
            String symbol = switch (type) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
            return textColor + symbol + RESET_TEXT_COLOR;
        }
    }

    private static void drawLetterLabels(StringBuilder out, List<String> labels) {
        out.append(SET_BG_COLOR_BLACK);
        for (String label : labels) {
            out.append(label);
        }
        out.append(RESET_BG_COLOR + "\n");
    }
}
