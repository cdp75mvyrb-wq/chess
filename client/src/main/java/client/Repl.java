package client;

import result.LoginResult;
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
            if (authToken == null) {System.out.print("[LOGGED OUT] >> ");}
            else {System.out.print("[LOGGED IN] >> ");}
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
            //logged out
            if (authToken == null) {
                return switch (commandWord) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "help" -> help();
                    default -> """
                            Your input could not be interpreted!
                            Type "help" to see possible commands
                            """;
                };
            }
            //logged in
            else {
                return switch (commandWord) {
                    case "logout" -> "logout not implemented yet";
                    default -> help();
                };
            }
        }
        catch (Exception e) {return e.getMessage();}
    }

    private String login(String[] params) throws Exception {
        if (params.length != 2) {return "Incorrect login formatting.\nFormat: login <USERNAME> <PASSWORD>";}
        LoginResult result = facade.login(params[0], params[1]);
        this.authToken = result.authToken();
        return "Logged in as " + result.username();
    }

    private String register(String[] params) throws Exception {
        if (params.length != 3) {return "Incorrect register formatting.\nFormat: register <USERNAME> <PASSWORD> <EMAIL>";}
        RegisterResult result = facade.register(params[0], params[1], params[2]);
        this.authToken = result.authToken();
        return "Registered and logged in as " + result.username();
    }

    private String help() {
        String out = """
                register <USERNAME> <PASSWORD> <EMAIL> - create an account
                login <USERNAME> <PASSWORD> - play chess
                quit - exit chess program
                help - see possible commands
                """;
        String in = """
                create <NAME> - chess game
                join <ID> [WHITE|BLACK] - chess game
                observe <ID> - chess game
                logout - exit user session
                quit - exit chess program
                help - see possible commands
                """;
        return switch (authToken) {
            case null -> out;
            default -> in;
        };
    }
}
