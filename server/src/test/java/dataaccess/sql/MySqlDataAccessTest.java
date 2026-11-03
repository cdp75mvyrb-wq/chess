package dataaccess.sql;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataAccessTest {
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
}