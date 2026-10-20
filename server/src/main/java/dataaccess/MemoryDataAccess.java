package dataaccess;

import chess.ChessGame;
import com.sun.security.jgss.AuthorizationDataEntry;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public class MemoryDataAccess implements DataAccess {

    final Map<String, UserData> users = new HashMap<>();
    final Map<Integer, GameData> games = new HashMap<>();
    final Map<String, AuthData> auths = new HashMap<>();

    private int nextGameId = 1;

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData game) {
        games.put(game.gameID(), game);
    }

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void createAuth(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public int createGame(String gameName) {
        nextGameId += 1;
        GameData newGame = new GameData(nextGameId, null, null, gameName, new ChessGame());
        games.put(newGame.gameID(), newGame);
        return newGame.gameID();
    }

    @Override
    public void clearUsers() throws DataAccessException {
        users.clear();
    }

    @Override
    public void clearAuthTokens() throws DataAccessException {
        auths.clear();
    }

    @Override
    public void clearGames() throws DataAccessException {
        games.clear();
    }
}