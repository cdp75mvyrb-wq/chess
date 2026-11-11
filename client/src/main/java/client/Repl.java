package client;

import chess.ChessBoard;
import model.GameData;
import result.CreateGameResult;
import result.LoginResult;
import result.RegisterResult;
import ui.BoardPrinter;

import static ui.EscapeSequences.*;

import java.util.*;

public class Repl {
    private final ServerFacade facade;
    private String authToken = null;
    private String username = null;
    private ArrayList<GameData> lastListedGames = new ArrayList<>();

    public Repl(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(SET_TEXT_BOLD +
                SET_TEXT_COLOR_MAGENTA +
                "Welcome to CS 240 Chess!\n" +
                SET_TEXT_COLOR_YELLOW +
                SET_TEXT_ITALIC +
                RESET_TEXT_BOLD_FAINT +
                "Type 'help' to get started" +
                RESET_TEXT_ITALIC);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (authToken == null) {System.out.print(SET_TEXT_COLOR_RED +
                    "[LOGGED OUT] >> " +
                    RESET_TEXT_COLOR);}
            else {System.out.print(SET_TEXT_COLOR_GREEN +
                    "[LOGGED IN] >> " +
                    RESET_TEXT_COLOR);}
            String result = eval(scanner.nextLine());
            if (result ==  "quit") {
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
                    case "quit" -> "quit";
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
                    case "quit" -> "quit";
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
        return BoardPrinter.draw(lastListedGames.get(id - 1).game().getBoard(), "WHITE");
    }

    private String join(String[] params) {
        //check color input
        String color = params[1].toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            return "Color must be WHITE or BLACK.";
        }
        return null;
    }

    private String list() throws Exception {
        System.out.println("- CURRENT ACTIVE GAMES -");
        this.lastListedGames = new ArrayList<>(facade.listGames(authToken).games());
        int index = 1;
        for (GameData game : lastListedGames) {
            System.out.println(index + ": " + game.gameName());
            index += 1;
        }
        return "- END OF LIST -";
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
        return SET_TEXT_ITALIC +
                SET_TEXT_COLOR_LIGHT_GREY +
                "Logged in as: " +
                RESET_TEXT_ITALIC +
                SET_TEXT_COLOR_WHITE +
                result.username() +
                RESET_BG_COLOR;
    }

    private String register(String[] params) throws Exception {
        if (params.length != 3) {return "Incorrect register formatting.\nFormat: register <USERNAME> <PASSWORD> <EMAIL>";}
        RegisterResult result = facade.register(params[0], params[1], params[2]);
        this.authToken = result.authToken();
        this.username = result.username();
        return "Registered and logged in as " + result.username();
    }

    private String help() {
        String out = SET_TEXT_COLOR_BLUE + """
                register <USERNAME> <PASSWORD> <EMAIL> - create an account
                login <USERNAME> <PASSWORD> - play chess
                quit - exit chess program
                help - see possible commands
                """ +
                RESET_TEXT_COLOR;
        String in = SET_TEXT_COLOR_BLUE + """
                create <NAME> - chess game
                join <ID> [WHITE|BLACK] - chess game
                observe <ID> - chess game
                logout - exit user session
                quit - exit chess program
                help - see possible commands
                """ +
                RESET_TEXT_COLOR;
        return switch (authToken) {
            case null -> out;
            default -> in;
        };
    }
}
