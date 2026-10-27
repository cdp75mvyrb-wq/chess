package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import request.*;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import service.GameService;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

import javax.naming.AuthenticationException;

public class GameHandler {

    private final GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {this.gameService = gameService;}

    public void create(Context ctx) throws BadRequestException, DataAccessException, UnauthorizedException {
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(request, ctx.header("authorization"));
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }

    public void list(Context ctx) throws DataAccessException, UnauthorizedException {
        ListGamesResult result = gameService.listGames(ctx.header("authorization"));
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }

    public void join(Context ctx) throws DataAccessException, BadRequestException, AuthenticationException {
        JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);
        JoinGameResult result = gameService.joinGame(request, ctx.header("authorization"));
    }
}
