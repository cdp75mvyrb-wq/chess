package server;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import handler.GameHandler;
import handler.UserHandler;
import io.javalin.*;
import io.javalin.http.Context;
import service.AuthService;
import service.ClearService;
import handler.ClearHandler;
import service.GameService;
import service.UserService;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

import javax.naming.ldap.spi.LdapDnsProvider;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        DataAccess dataAccess = new MemoryDataAccess();
        ClearService clearService = new ClearService(dataAccess);
        ClearHandler clearHandler = new ClearHandler(clearService);
        AuthService authService = new AuthService(dataAccess);
        GameService gameService = new GameService(dataAccess, authService);
        UserService userService = new UserService(dataAccess, authService);
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
        javalin.delete("/db", clearHandler::clear);
        javalin.post("/game", gameHandler::create);
        javalin.get("/game", gameHandler::list);

        // exception handlers

        javalin.exception(BadRequestException.class,   (e, ctx) -> sendError(ctx, 400, e.getMessage()));
        javalin.exception(AlreadyTakenException.class, (e, ctx) -> sendError(ctx, 403, e.getMessage()));
        javalin.exception(DataAccessException.class,   (e, ctx) -> sendError(ctx, 500, e.getMessage()));
        javalin.exception(UnauthorizedException.class,   (e, ctx) -> sendError(ctx, 401, e.getMessage()));
    }

    private void sendError(Context ctx, int status, String message) {
        ctx.status(status);
        ctx.result(new Gson().toJson(Map.of("message", message)));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}