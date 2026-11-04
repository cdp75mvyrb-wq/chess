package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

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
        var DAO = new MySqlDataAccess();
        assertDoesNotThrow(() -> DAO.clearUsers());
    }

    @Test
    void clearAuthSuccess() throws DataAccessException {
        var DAO = new MySqlDataAccess();
        assertDoesNotThrow(() -> DAO.clearAuthTokens());
    }

    @Test
    void clearGameSuccess() throws DataAccessException {
        var DAO = new MySqlDataAccess();
        assertDoesNotThrow(() -> DAO.clearGames());
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
}