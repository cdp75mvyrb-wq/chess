package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import model.UserData;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;
    private final AuthService authService;

    public UserService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, BadRequestException, AlreadyTakenException {
        if (request == null || request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (dataAccess.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        } else {
            dataAccess.createUser(new UserData(request.username(), request.password(), request.email()));
            AuthData authData = createAuthFor(request.username());
            dataAccess.createAuth(authData);
            return new RegisterResult(request.username(), authData.authToken());
        }
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, BadRequestException, UnauthorizedException {
        if (request == null || request.username() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (dataAccess.getUser(request.username()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        else {
            UserData userData = dataAccess.getUser(request.username());
            if (!userData.password().equals(request.password())) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            AuthData authData = createAuthFor(request.username());
            dataAccess.createAuth(authData);
            return new LoginResult(request.username(), authData.authToken());
        }
    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException, UnauthorizedException {
        authService.authenticate(request.authToken());
        dataAccess.deleteAuth(request.authToken());
        return new LogoutResult();
    }

    private AuthData createAuthFor(String username) {
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }
}
