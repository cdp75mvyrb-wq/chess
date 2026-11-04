package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataAccessTest {
    MySqlDataAccess dataAccess;

    @BeforeEach
    void setup() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        dataAccess.clearAuthTokens();
        dataAccess.clearUsers();
        dataAccess.clearGames();
    }

    @Test
    void constructorCreatesTables() {
        assertDoesNotThrow(() -> new MySqlDataAccess());
    }

    @Test
    void clearUserSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.clearUsers());
    }

    @Test
    void clearAuthSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.clearAuthTokens());
    }

    @Test
    void clearGameSuccess() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.clearGames());
    }

    @Test
    void addGetAuth() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.createAuth(new AuthData("hello", "sam")));
        assertEquals("sam", dataAccess.getAuth("hello").username());
    }

    @Test
    void addDeleteAuth() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.createAuth(new AuthData("hello", "sam")));
        assertDoesNotThrow(() -> dataAccess.deleteAuth("hello"));
        assertNull(dataAccess.getAuth("hello"));
    }

    @Test
    void addGetUser() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.createUser(new UserData("hello", "sam", "byu@edu")));
        assertEquals("byu@edu", dataAccess.getUser("hello").email());
    }

    @Test
    void createGetGame() throws DataAccessException {
        int gameID = dataAccess.createGame("sam's game");
        assertEquals("sam's game", dataAccess.getGame(gameID).gameName());
    }

    @Test
    void updateGame() throws DataAccessException {
        int gameID = dataAccess.createGame("sam's game");
        GameData originalData = dataAccess.getGame(gameID);
        dataAccess.updateGame(new GameData(gameID, null, null, "game", originalData.game()));
        assertEquals("game", dataAccess.getGame(gameID).gameName());
    }

    @Test
    void getGamesCorrect() throws DataAccessException {
        int id1 = dataAccess.createGame("game 1");
        int id2 = dataAccess.createGame("game 2");
        int id3 = dataAccess.createGame("game 3");
        Collection<GameData> games = dataAccess.listGames();
        assertEquals(3, games.size());
        assertTrue(games.contains(new GameData(id1, null, null, "game 1", new ChessGame())));
        assertTrue(games.contains(new GameData(id2, null, null, "game 2", new ChessGame())));
        assertTrue(games.contains(new GameData(id3, null, null, "game 3", new ChessGame())));
    }
}