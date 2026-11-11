package client;

import model.GameData;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;

import java.util.*;

public class Repl {
    private final ServerFacade facade;
    private String authToken = null;
    private String username = null;
    private Collection<GameData> lastListedGames = new ArrayList<>();

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
                    case "logout" -> logout();
                    case "create" -> create(params);
                    case "list" -> list();
                    case "join" -> join(params);
                    case "observe" -> observe(params);
                    default -> help();
                };
            }
        }
        catch (Exception e) {return e.getMessage();}
    }

    private String observe(String[] params) {
        if (params.length != 1) {return "Incorrect formatting.\nFormat: observe <ID>\nType 'list' to see possible games to observe.";}
        int id = Integer.parseInt(params[0]);
        if (id < 1 || id > lastListedGames.size()) {
            return "ID does not exist.\nType 'list' to see possible games to observe.";
        }
        return "List games now";
    }

    private String join(String[] params) {
        return null;
    }

    private String list() throws Exception {
        System.out.println("Current active games:");
        this.lastListedGames = facade.listGames(authToken).games();
        int index = 1;
        for (GameData game : lastListedGames) {
            System.out.println(index + ": " + game.gameName());
            index += 1;
        }
        return "END OF LIST";
    }

    private String create(String[] params) throws Exception {
        if (params.length != 1) {return "Incorrect formatting.\nFormat: create <NAME>";}
        CreateGameResult result = facade.createGame(authToken, params[0]);
        return "Game created!\nYour game ID is: " + result.gameID();
    }

    private String logout() throws Exception {
        facade.logout(authToken);
        return "Logged out of " + this.username + "'s session";
    }

    private String login(String[] params) throws Exception {
        if (params.length != 2) {return "Incorrect login formatting.\nFormat: login <USERNAME> <PASSWORD>";}
        LoginResult result = facade.login(params[0], params[1]);
        this.authToken = result.authToken();
        this.username = result.username();
        return "Logged in as " + result.username();
    }

    private String register(String[] params) throws Exception {
        if (params.length != 3) {return "Incorrect register formatting.\nFormat: register <USERNAME> <PASSWORD> <EMAIL>";}
        RegisterResult result = facade.register(params[0], params[1], params[2]);
        this.authToken = result.authToken();
        this.username = result.username();
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
