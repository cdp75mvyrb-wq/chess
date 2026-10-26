package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import service.ClearService;
import service.UserService;

public class UserHandler {

    UserService userService;
    private final Gson gson = new Gson();

    public UserHandler(UserService userService) {this.userService = userService;}

    public void register(Context ctx) throws DataAccessException {
        RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }

    public void login(Context ctx) throws DataAccessException {
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        ctx.status(200);
        ctx.result(gson.toJson(result));
    }
}
