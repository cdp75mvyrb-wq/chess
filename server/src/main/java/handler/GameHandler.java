package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import request.CreateGameRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import service.GameService;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

public class GameHandler {

    private final GameService gameService;
    private final Gson gson = new Gson();

    public GameHandler(GameService gameService) {this.gameService = gameService;}

    public void create(@NotNull Context ctx) throws BadRequestException, DataAccessException, UnauthorizedException {
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult result = gameService.createGame(request, ctx.header("authorization"));
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
}
