package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataAccessTest {
    MySqlDataAccess dataAccess;

    @BeforeEach
    void setup() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
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

    void addGetUser() throws DataAccessException {
        assertDoesNotThrow(() -> dataAccess.createUser(new UserData("hello", "sam", "byu@edu")));
        assertEquals("byu.edu", dataAccess.getUser("hello").email());
    }
}