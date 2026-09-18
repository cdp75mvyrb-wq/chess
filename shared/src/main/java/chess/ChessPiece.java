package chess;

import java.util.List;
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
        List<ChessMove> moves = new ArrayList<>();
        int [][] diagonalDirections = {{1,1},{1,-1},{-1,1},{-1,-1}};
        int [][] straightDirections = {{1,0},{-1,0},{0,1},{0,-1}};
        switch (type) {
            case BISHOP:
                moves.addAll(slideMoves(board, myPosition, diagonalDirections));
                break;
            case ROOK:
                moves.addAll(slideMoves(board, myPosition, straightDirections));
                break;
            case QUEEN:
                moves.addAll(slideMoves(board, myPosition, diagonalDirections));
                moves.addAll(slideMoves(board, myPosition, straightDirections));
                break;
            case KING:
                int[][] kingDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
                moves.addAll(staticMoves(board, myPosition, kingDirections));
                break;
            case KNIGHT:
                int[][] knightDirections = {{1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
                moves.addAll(staticMoves(board, myPosition, knightDirections));
                break;
            case PAWN:
                moves.addAll(pawnMoves(board, myPosition));
                break;
        }
        return moves;
    }

    private Collection<ChessMove> slideMoves(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
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

    private Collection<ChessMove> staticMoves(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int row = myPosition.getRow() + rowOffset;
            int col = myPosition.getColumn() + colOffset;
            //skip current position or out of bounds moves
            if ((row < 1 || row > 8) || (col < 1 || col > 8)) {
                continue;
            }
            //empty space or capturable
            else if ((board.getPiece(new ChessPosition(row, col)) == null) || ((board.getPiece(new ChessPosition(row, col)).getTeamColor() != color))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        switch (color) {
            case WHITE:
                //forward movements
                if (myRow + 1 <= 8 && (board.getPiece(new ChessPosition(myRow + 1, myCol)) == null)) {
                    //check if on starting position
                    if ((myRow == 2) && (board.getPiece(new ChessPosition(myRow + 2, myCol)) == null)) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 2, myCol), null));
                    }
                    if (myRow + 1 == 8) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol), PieceType.BISHOP));
                    } else {moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol), null));}
                }
//                attacking
                if (myRow + 1 <= 8 && myCol + 1 <= 8 && myRow + 1 >= 1 && myCol + 1 >= 1 && (board.getPiece(new ChessPosition(myRow + 1, myCol + 1)) != null && (board.getPiece(new ChessPosition(myRow + 1, myCol + 1)).getTeamColor() != color))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), null));
                    if (myRow + 1 == 8) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), PieceType.BISHOP));
                    } else {moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol + 1), null));}
                }
                if (myRow + 1 <= 8 && myCol - 1 <= 8 && myRow + 1 >= 1 && myCol - 1 >= 1 && (board.getPiece(new ChessPosition(myRow + 1, myCol - 1)) != null && (board.getPiece(new ChessPosition(myRow + 1, myCol - 1)).getTeamColor() != color))) {
                    if (myRow + 1 == 8) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), PieceType.BISHOP));
                    } else {moves.add(new ChessMove(myPosition, new ChessPosition(myRow + 1, myCol - 1), null));}
                }
                break;
            case BLACK:
                //forward movements
                if (myRow - 1 <= 8 && (board.getPiece(new ChessPosition(myRow - 1, myCol)) == null)) {
                    //check if on starting position
                    if ((myRow == 7) && (board.getPiece(new ChessPosition(myRow - 2, myCol)) == null)) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 2, myCol), null));
                    }
                    if (myRow - 1 == 1) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol), PieceType.BISHOP));
                    } else {moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol), null));}
                }
                if ((myRow - 1 <= 8 && myCol - 1 <= 8 && myRow - 1 >= 1 && myCol - 1 >= 1 && board.getPiece(new ChessPosition(myRow - 1, myCol - 1)) != null && (board.getPiece(new ChessPosition(myRow - 1, myCol - 1)).getTeamColor() != color))) {
                    if (myRow - 1 == 1) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), PieceType.BISHOP));
                    } else {moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol - 1), null));}
                }
                if ((myRow - 1 <= 8 && myCol + 1 <= 8 && myRow - 1 >= 1 && myCol + 1 >= 1) && (board.getPiece(new ChessPosition(myRow - 1, myCol + 1)) != null && (board.getPiece(new ChessPosition(myRow - 1, myCol + 1)).getTeamColor() != color))) {
                    if (myRow - 1 == 1) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), PieceType.BISHOP));
                    } else {moves.add(new ChessMove(myPosition, new ChessPosition(myRow - 1, myCol + 1), null));}
                }
                break;
        }
        return moves;
    }
}
