package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

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
    void registerSuccess() throws Exception {
        String username = "sam";
        RegisterResult result = userService.register(new RegisterRequest(username, "1234", "bruh@gmail.com"));
        assertEquals(username, result.username());
        assertEquals(dataAccess.getAuth(result.authToken()).username(), username);
    }

    @Test
    void registerNullRequest() throws Exception {
        assertThrows(BadRequestException.class,
                () -> userService.register(null));
    }

    @Test
    void registerAlreadyTakenUsername() throws Exception {
        String username = "sam";
        userService.register(new RegisterRequest(username, "1234", "bruh@gmail.com"));
        assertThrows(AlreadyTakenException.class,
                () -> userService.register(new RegisterRequest(username, "1234", "bruh@gmail.com")));
    }

    @Test
    void loginSuccess() throws Exception {
        String password = "1234";
        userService.register(new RegisterRequest("sam", password, "bruh@gmail.com"));
        assertEquals("sam", userService.login(new LoginRequest("sam", password)).username());
    }

    @Test
    void loginNullRequest() throws Exception {
        assertThrows(BadRequestException.class,
                () -> userService.login(null));
    }

    @Test
    void loginBadPassword() throws Exception {
        userService.register(new RegisterRequest("sam", "1234", "bruh@gmail.com"));
        assertThrows(UnauthorizedException.class,
                () -> userService.login(new LoginRequest("sam", "4321")));
    }
}