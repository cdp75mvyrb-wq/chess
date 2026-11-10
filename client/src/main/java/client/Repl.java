package client;

public class Repl {
    ServerFacade facade;

    public Repl(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }
}
