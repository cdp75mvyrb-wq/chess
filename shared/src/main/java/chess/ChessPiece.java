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

    ChessGame.TeamColor color;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
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
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] diagonalDirections = new int[][]{{1,1},{-1,1},{1,-1},{-1,-1}};
        int [][] straightDirections = new int[][]{{1,0},{-1,0},{0,-1},{0,1}};
        switch (type) {
            case BISHOP:
                moves.addAll(slideMoves(board, myPosition, diagonalDirections));
                break;
            case ROOK:
                moves.addAll(slideMoves(board, myPosition, straightDirections));
                break;
            case QUEEN:
                moves.addAll(slideMoves(board, myPosition, straightDirections));
                moves.addAll(slideMoves(board, myPosition, diagonalDirections));
                break;
            case KING:
                moves.addAll(staticMoves(board, myPosition, new int[][]{{1,1},{-1,-1},{1,-1},{-1,1},{1,0},{0,1},{-1,0},{0,-1}}));
                break;
            case KNIGHT:
                moves.addAll(staticMoves(board, myPosition, new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}}));
                break;
            case PAWN:
                moves.addAll(pawnMoves(board, myPosition));
                break;
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        boolean white = color == ChessGame.TeamColor.WHITE;
        int startRow = white ? 2 : 7;
        int direction = white ? 1 : -1;
        int promoRow = white ? 8 : 1;
        //forward moves
        int oneStep = row + direction;
        if (inBounds(oneStep, col) && board.getPiece(new ChessPosition(oneStep, col)) == null) {
            addPawnMove(board, moves, position, oneStep, col, promoRow);
            int twoStep = row + 2 * direction;
            if (startRow == row && board.getPiece(new ChessPosition(twoStep, col)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(twoStep, col), null));
            }
        }
        //capture moves
        for (int i : new int[]{1,-1}) {
            int colStep = col + i;
            if (inBounds(oneStep, colStep) && board.getPiece(new ChessPosition(oneStep, colStep)) != null) {
                if (board.getPiece(new ChessPosition(oneStep, colStep)).getTeamColor() != color) {
                    addPawnMove(board, moves, position, oneStep, colStep, promoRow);
                }
            }
        }

        return moves;
    }

    private void addPawnMove(ChessBoard board, Collection<ChessMove> moves, ChessPosition current, int toRow, int toCol, int promoRow) {
        ChessPosition target = new ChessPosition(toRow, toCol);
        if (toRow == promoRow) {
            moves.add(new ChessMove(current, target, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(current, target, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(current, target, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(current, target, ChessPiece.PieceType.ROOK));
        } else {moves.add(new ChessMove(current, target, null));}
    }

    private Collection<ChessMove> slideMoves(ChessBoard board, ChessPosition position, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int colOffset = direction[1];
            int curRow = row + rowOffset;
            int curCol = col + colOffset;
            while (inBounds(curRow, curCol)) {
                //empty space
                if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(curRow, curCol), null));
                    curRow += rowOffset;
                    curCol += colOffset;
                }
                //capturable
                else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(curRow, curCol), null));
                    break;
                }
                //friendly
                else {break;}
            }
        }
        return moves;
    }

    private Collection<ChessMove> staticMoves(ChessBoard board, ChessPosition position, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        for (int[]direction : directions) {
            int targetRow = row + direction[0];
            int targetCol = col + direction[1];
            ChessPosition target = new ChessPosition(targetRow, targetCol);
            if (inBounds(targetRow, targetCol) && (board.getPiece(target) == null || board.getPiece(target).getTeamColor() != color)) {
                moves.add(new ChessMove(position,target,null));
            }
        }
        return moves;
    }

    private boolean inBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
