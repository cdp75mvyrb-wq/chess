package client;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        String hardcodedURL = "http://localhost:8080";
        ServerFacade serverFacade = new ServerFacade(hardcodedURL);

        serverFacade.clear();

        String authToken = serverFacade.register("sam", "1234", "hi@byu.edu").authToken();
        int gameID1 = serverFacade.createGame(authToken, "game1").gameID();
        int gameID2 = serverFacade.createGame(authToken, "game2").gameID();
        int gameID3 = serverFacade.createGame(authToken, "game3").gameID();
        System.out.println(serverFacade.listGames("bad token").games());
    }
}