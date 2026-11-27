package websocket.commands;

import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getChessMove() {
        return this.move;
    }
}