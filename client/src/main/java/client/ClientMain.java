package client;

import result.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        String hardcodedURL = "http://localhost:8080";
        ServerFacade serverFacade = new ServerFacade(hardcodedURL);
        RegisterResult result = serverFacade.register("sam", "1234", "sam@byu.edu");
        System.out.println("Authentication token: " + result.authToken());
    }
}