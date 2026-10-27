package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import request.CreateGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

import java.util.Collection;

public class GameService {

    private final DataAccess dataAccess;
    private final AuthService authService;

    public GameService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws BadRequestException, DataAccessException, UnauthorizedException {
        if (request == null || request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        authService.authenticate(authToken);
        int gameID = dataAccess.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException, UnauthorizedException {
        if (authToken == null) {
            throw new BadRequestException("Error: bad request");
        }
        authService.authenticate(authToken);
        Collection<GameData> games = dataAccess.listGames();
        return new ListGamesResult(games);
    }
}
