package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;

    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (board[position.getRow() - 1][position.getColumn() - 1] != null) {
            return board[position.getRow() - 1][position.getColumn() - 1];
        } else {return null;}
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
//      back rows
        ChessPiece.PieceType[] backRow = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };
        for (int i = 1; i <= 8; i++) {
            System.out.println(i);
            addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, backRow[i - 1]));
            System.out.println("W " + backRow[i - 1]);
            addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, backRow[i - 1]));
            System.out.println("B " + backRow[i - 1]);
        }
        System.out.println("Board Complete");
    }
}
