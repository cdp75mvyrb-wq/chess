package dataaccess;

import com.sun.security.jgss.AuthorizationDataEntry;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public class MemoryDataAccess implements DataAccess {

    final Map<String, UserData> users = new HashMap<>();
    final Map<Integer, GameData> games = new HashMap<>();
    final Map<String, AuthData> auths = new HashMap<>();

    public void clear() {

    }

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
    public void updateGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public void createUser(UserData userData) {

    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {

    }

    @Override
    public void clearAuthTokens() throws DataAccessException {

    }

    @Override
    public void clearGames() throws DataAccessException {

    }
}