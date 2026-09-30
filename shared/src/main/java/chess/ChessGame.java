package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;
    private ChessMove lastMove;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece startPiece = board.getPiece(startPosition);
        TeamColor color = startPiece.getTeamColor();
        // square is empty
        if (startPiece == null) {
            return null;
        }
        // collect all possible moves
        Collection<ChessMove> allMoves = startPiece.pieceMoves(board, startPosition);
        // add any en passant possible moves
        allMoves.addAll(enPassantMoves(startPosition, piece));

        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : allMoves) {
            // make copy
            ChessBoard simulationBoard = board.copy();
            ChessPiece movingPiece = new ChessPiece(color,startPiece.getPieceType());
            simulationBoard.movePiece(movingPiece, move);
            if (!isInCheck(color, simulationBoard)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece != null && piece.getTeamColor() == getTeamTurn() && validMoves(move.getStartPosition()).contains(move)) {
            board.movePiece(piece, move);
            boolean whiteTurn = getTeamTurn() == TeamColor.WHITE;
            setTeamTurn(whiteTurn ? TeamColor.BLACK : TeamColor.WHITE);
            lastMove = move;
        } else {
            throw new InvalidMoveException("Move invalid");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // (same board pass):
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition square = new ChessPosition(row, col);
                ChessPiece piece = this.board.getPiece(square);
                // skip empty squares
                if (piece == null) {continue;}
                // get team's king position
                else if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = square;
                }
                // get enemy pieces / moves
                else if (piece.getTeamColor() != teamColor) {
                    enemyMoves.addAll(piece.pieceMoves(this.board, square));
                }
            }
        }
        for (ChessMove move : enemyMoves) {
            ChessPosition endPosition = move.getEndPosition();
            if (endPosition.equals(kingPosition)) {
                return true;
            }
        }
        // no pieces can attack king
        return false;
    }

    // same function as before, only private and takes board as a parameter rather than using this.board
    // used in simulation boards only

    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        // (same board pass):
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition square = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(square);
                // skip empty squares
                if (piece == null) {continue;}
                // get team's king position
                else if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = square;
                }
                // get enemy pieces / moves
                else if (piece.getTeamColor() != teamColor) {
                    enemyMoves.addAll(piece.pieceMoves(board, square));
                }
            }
        }
        for (ChessMove move : enemyMoves) {
            ChessPosition endPosition = move.getEndPosition();
            if (endPosition.equals(kingPosition)) {
                return true;
            }
        }
        // no pieces can attack king
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && !hasValidMoves(teamColor)) {
            return true;
        }
        return false;
    }

    private boolean hasValidMoves(TeamColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition position = new ChessPosition(row + 1, col + 1);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == color) {
                    if (!validMoves(position).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && !hasValidMoves(teamColor)) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }
}
