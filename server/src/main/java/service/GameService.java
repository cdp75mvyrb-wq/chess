package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import service.exception.AlreadyTakenException;
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

    public JoinGameResult joinGame(JoinGameRequest request, String authToken) throws DataAccessException, UnauthorizedException, BadRequestException, AlreadyTakenException {
        if (request == null) {throw new BadRequestException("Error: bad request");}
        if (request.gameID() == 0 || request.playerColor() == null || (!request.playerColor().equals("BLACK") && !request.playerColor().equals("WHITE"))) {
            throw new BadRequestException("Error: bad request");
        }
        String color = request.playerColor();
        int gameID = request.gameID();
        authService.authenticate(authToken);
        GameData gameData = dataAccess.getGame(gameID);
        if (gameData == null) {throw new BadRequestException("Error: bad request");}
        if (color.equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else {
                GameData newGameData = new GameData(gameData.gameID(), dataAccess.getAuth(authToken).username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
                dataAccess.updateGame(newGameData);
            }
        } else {
            if (gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else {
                GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), dataAccess.getAuth(authToken).username(), gameData.gameName(), gameData.game());
                dataAccess.updateGame(newGameData);
            }
        }
        return new JoinGameResult();
    }
}
