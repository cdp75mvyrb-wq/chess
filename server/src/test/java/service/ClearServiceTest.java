package service;

import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    MemoryDataAccess dataAccess;
    ClearService clearService;

    @BeforeEach
    void setup() {
        dataAccess = new MemoryDataAccess();
        clearService = new ClearService(dataAccess);
    }

    @Test
    void clear() throws Exception {
        String token = "afa1235ey89vw45";
        dataAccess.createUser(new UserData("sam", "1234", "scsykes@icloud.com"));
        dataAccess.createAuth(new AuthData(token, "sam"));
        dataAccess.createGame("Sam's Game");
        clearService.clear();
        assertNull(dataAccess.getAuth(token));
        assertNull(dataAccess.getUser("sam"));
        assertTrue(dataAccess.listGames().isEmpty());
    }
}