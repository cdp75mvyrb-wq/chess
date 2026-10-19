package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    GameData getGame(int gameID);

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(GameData gameData);

    void createUser(UserData userData);
    void createAuth(AuthData authData);
    void deleteAuth(String authToken);
    GameData createGame(String gameName);

    void clearUsers() throws DataAccessException;
    void clearAuthTokens() throws DataAccessException;
    void clearGames() throws DataAccessException;
}