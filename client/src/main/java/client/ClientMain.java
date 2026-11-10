package client;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        String hardcodedURL = "http://localhost:8080";
        ServerFacade serverFacade = new ServerFacade(hardcodedURL);

    }
}