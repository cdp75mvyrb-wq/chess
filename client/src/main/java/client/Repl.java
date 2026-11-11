package client;

import result.RegisterResult;

import java.util.Arrays;
import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private String authToken = null;

    public Repl(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to CS 240 Chess. Type help to get started");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (authToken == null) {System.out.println("[LOGGED OUT] >> ");}
            else {System.out.println("[LOGGED IN] >> ");}
            String result = eval(scanner.nextLine());
            if (result.equals("quit")) {
                break;
            }
            System.out.println(result);
        }
    }

    private String eval(String line) {
        String[] tokens = line.trim().split("\\s+");
        String commandWord = tokens[0].toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            if (authToken == null) {
                return switch (commandWord) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    default -> "help string not created yet";
                };
            }
            return switch (commandWord) {
                case "logout" -> "logout not implemented yet";
                default -> "help string not created yet";
            };
        }
        catch (Exception e) {return e.getMessage();}
    }

    private String login(String[] params) {
        return "login method not implemented yet";
    }

    private String register(String[] params) throws Exception {
        if (params.length != 3) {return "Format: register <USERNAME> <PASSWORD> <EMAIL>";}
        RegisterResult result = facade.register(params[0], params[1], params[2]);
        this.authToken = result.authToken();
        return "Registered and logged in as " + result.username();
    }
}
