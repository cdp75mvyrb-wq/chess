package client;

import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        String hardcodedURL = "http://localhost:8080";
        ServerFacade serverFacade = new ServerFacade(hardcodedURL);
        RegisterResult registerResult = serverFacade.register("sam", "1234", "sam@byu.edu");
        System.out.println("Authentication token: " + registerResult.authToken());

        serverFacade.logout(registerResult.authToken());
        System.out.println("Logged out register session");

        LoginResult loginResult = serverFacade.login("sam", "1234");
        System.out.println("Authentication token: " + loginResult.authToken());

        LogoutResult logoutResult = serverFacade.logout(loginResult.authToken());
        System.out.println("Logged out login session");
    }
}