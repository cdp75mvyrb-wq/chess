package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        return color + " " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (type) {
            case BISHOP:
                moves.addAll(diagonalMoves(board, myPosition));
                break;
            case ROOK:
                moves.addAll(straightMoves(board, myPosition));
                break;
            case QUEEN:
                moves.addAll(straightMoves(board, myPosition));
                moves.addAll(diagonalMoves(board, myPosition));
                break;
        }
        return moves;
    }

    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        //list of directions (up right, up left, down right, down left)
        int [][] directions = {{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int row = myPosition.getRow() + rowOffset;
            int col = myPosition.getColumn() + colOffset;
            while ((1 <= row) && (row <= 8) && (1 <= col) && (col <= 8)) {
                //empty space
                if (board.getPiece(new ChessPosition(row,col)) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                    row += rowOffset;
                    col += colOffset;
                }
                //enemy piece
                else if (board.getPiece(new ChessPosition(row,col)).getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                    break;
                }
                //friendly piece
                else if (board.getPiece(new ChessPosition(row,col)).getTeamColor() == color) {
                    break;
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> straightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        //list of directions (right, left, up, down)
        int [][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int row = myPosition.getRow() + rowOffset;
            int col = myPosition.getColumn() + colOffset;
            while ((1 <= row) && (row <= 8) && (1 <= col) && (col <= 8)) {
                //empty space
                if (board.getPiece(new ChessPosition(row,col)) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                    row += rowOffset;
                    col += colOffset;
                }
                //enemy piece
                else if (board.getPiece(new ChessPosition(row,col)).getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                    break;
                }
                //friendly piece
                else if (board.getPiece(new ChessPosition(row,col)).getTeamColor() == color) {
                    break;
                }
            }
        }
        return moves;
    }
}
