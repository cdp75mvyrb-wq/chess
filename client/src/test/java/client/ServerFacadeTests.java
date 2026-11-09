package client;

import org.junit.jupiter.api.*;
import result.LoginResult;
import result.RegisterResult;
import server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exception.BadRequestException;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws Exception {
        facade.clear();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    void registerSuccess() throws Exception {
        assertDoesNotThrow(() -> facade.register("sam", "1234", "sam@byu.edu"));
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("sam", "1234", "sam@byu.edu");
        assertDoesNotThrow(() -> facade.login("sam", "1234"));
    }

    @Test
    void loginFail() throws Exception {
        assertThrows(Exception.class,
                    () -> facade.login("sam", "1234"));
    }

    @Test
    void registerFail() throws Exception {
        facade.register("sam", "1234", "sam@byu.edu");
        assertThrows(Exception.class,
                () -> facade.register("sam", "1234", "sam@byu.edu"));
    }

    @Test
    void logoutFail() throws Exception {
        facade.register("sam", "1234", "sam@byu.edu");
        assertThrows(Exception.class,
                () -> facade.logout("wrong authToken"));
    }
}