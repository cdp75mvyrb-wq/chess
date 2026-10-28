package service;

import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    MemoryDataAccess dataAccess;
    GameService gameService;
    AuthService authService;
    UserService userService;

    @BeforeEach
    void setup() {
        dataAccess = new MemoryDataAccess();
        authService = new AuthService(dataAccess);
        gameService = new GameService(dataAccess, authService);
        userService = new UserService(dataAccess, authService);
    }

    @Test
    void createGameSuccess() throws Exception {
        createAuthUser();
        CreateGameResult result = gameService.createGame(new CreateGameRequest("Sam's Game"), "1");
        assertNotNull(dataAccess.getGame(result.gameID()));
    }

    @Test
    void createGameFail() {
        createAuthUser();
        assertThrows(BadRequestException.class,
                () -> gameService.createGame(new CreateGameRequest(null), "1"));
    }

    @Test
    void listGamesSuccess() throws Exception {
        createAuthUser();
        dataAccess.createGame("Game 1");
        dataAccess.createGame("Game 2");
        dataAccess.createGame("Game 3");
        ListGamesResult result = gameService.listGames("1");
        assertEquals(3, result.games().size());
    }

    @Test
    void listGamesFail() {
        createAuthUser();
        dataAccess.createGame("Game 1");
        dataAccess.createGame("Game 2");
        dataAccess.createGame("Game 3");
        assertThrows(BadRequestException.class,
                () -> gameService.listGames(null));
    }

    @Test
    void joinGameSuccess() throws Exception {
        createAuthUser();
        CreateGameResult result = gameService.createGame(new CreateGameRequest("Game 1"), "1");
        gameService.joinGame(new JoinGameRequest("WHITE", result.gameID()), "1");
        assertEquals("sam", dataAccess.getGame(result.gameID()).whiteUsername());
    }

    @Test
    void joinGameFail() throws Exception {
        createAuthUser();
        CreateGameResult result = gameService.createGame(new CreateGameRequest("Game 1"), "1");
        gameService.joinGame(new JoinGameRequest("WHITE", result.gameID()), "1");
        assertThrows(AlreadyTakenException.class,
                () -> gameService.joinGame(new JoinGameRequest("WHITE", result.gameID()), "1"));
    }

    void createAuthUser() {
        dataAccess.createUser(new UserData("sam", "1234", "bruh@gmail.com"));
        dataAccess.createAuth(new AuthData("1", "sam"));
    }

    @Test
    void joinGameBadAuth() throws Exception {
        createAuthUser();
        CreateGameResult result = gameService.createGame(new CreateGameRequest("Game 1"), "1");
        gameService.joinGame(new JoinGameRequest("WHITE", result.gameID()), "1");
        assertThrows(UnauthorizedException.class,
                () -> gameService.joinGame(new JoinGameRequest("BLACK", result.gameID()), "34"));
    }
}