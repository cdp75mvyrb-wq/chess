package client;

import chess.ChessGame;
import model.GameData;
import result.LoginResult;
import result.RegisterResult;
import ui.BoardPrinter;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

import java.net.ConnectException;
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
                SET_TEXT_COLOR_IVORY +
                "\nWelcome to CS 240 Chess!\n" +
                SET_TEXT_COLOR_SAGE +
                SET_TEXT_ITALIC +
                RESET_TEXT_BOLD_FAINT +
                "\n- Type 'help' to get started -\n" +
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
                    case "register" -> "\n" + register(params) + "\n";
                    case "login" -> "\n" + login(params) + "\n";
                    case "help" -> "\n" + help() + "\n";
                    case "quit" -> "\n" + "quit" + "\n";
                    default -> "\n" + help();
                };
            }
            //logged in
            else {
                return switch (commandWord) {
                    case "logout" -> "\n" + logout() + "\n";
                    case "create" -> "\n" + create(params) + "\n";
                    case "list" -> "\n" + list();
                    case "join" -> "\n" + join(params) + "\n";
                    case "observe" -> "\n" + observe(params) + "\n";
                    case "quit" -> "quit";
                    default -> "\n" + help();
                };
            }
        }
        catch (NumberFormatException e) {return "Game number must be a number.";}
        catch (ConnectException e) {return SET_TEXT_COLOR_RED +
                "\nCould not reach the server\n" +
                RESET_TEXT_COLOR +
                "\nPlease check your connection and try again\n";}
        catch (Exception e) {return e.getMessage();}
    }

    private String observe(String[] params) {
        if (params.length != 1) {return "Incorrect formatting.\nFormat: observe <ID>\nType 'list' to see possible games to observe.";}
        int id = Integer.parseInt(params[0]);
        if (id < 1 || id > lastListedGames.size()) {
            return "Game number does not exist.\nType 'list' to see possible games to observe.";
        }
        return BoardPrinter.draw(lastListedGames.get(id - 1).game().getBoard(), WHITE);
    }

    private String join(String[] params) throws Exception {
        if (params.length != 2) {return "Incorrect formatting.\nFormat: join <ID> [WHITE|BLACK]\nType 'list' to see possible games to join.";}
        int id = Integer.parseInt(params[0]);
        if (id < 1 || id > lastListedGames.size()) {
            return "Game number does not exist.\nType 'list' to see possible games to join.";
        }
        GameData game = lastListedGames.get(id - 1);
        String color = params[1].toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            return "Color must be WHITE or BLACK.";
        }
        facade.joinGame(authToken, color, game.gameID());
        return BoardPrinter.draw(game.game().getBoard(), ChessGame.TeamColor.valueOf(color));
    }

    private String list() throws Exception {
        this.lastListedGames = new ArrayList<>(facade.listGames(authToken).games());
        if (lastListedGames.isEmpty()) {return "- NO CURRENT ACTIVE GAMES -\n";}
        StringBuilder out = new StringBuilder();
        out.append("- CURRENT ACTIVE GAMES -\n");
        int index = 1;
        for (GameData game : lastListedGames) {
            out.append(index).append(": ").append(game.gameName()).append("\n");
            out.append("   White: ").append(formatPlayer(game.whiteUsername())).append("\n");
            out.append("   Black: ").append(formatPlayer(game.blackUsername())).append("\n");
            index += 1;
        }
        return out.toString();
    }

    private String formatPlayer(String username) {
        return username == null
                ? SET_TEXT_COLOR_GREEN + "OPEN" + RESET_TEXT_COLOR
                : SET_TEXT_COLOR_RED + username + RESET_TEXT_COLOR;
    }

    private String create(String[] params) throws Exception {
        if (params.length != 1) {return "Incorrect formatting.\nFormat: create <NAME>";}
        facade.createGame(authToken, params[0]);
        return "Created game '" + params[0] + "'.\nType 'list' to see active games.";
    }

    private String logout() throws Exception {
        facade.logout(authToken);
        this.authToken = null;
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
