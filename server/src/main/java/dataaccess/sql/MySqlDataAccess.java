package dataaccess.sql;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.List;

public class MySqlDataAccess implements DataAccess {
    private final String[] createStatements = {
        """
            CREATE TABLE IF NOT EXIST user (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            )
        """,
        """
            CREATE TABLE IF NOT EXIST game (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameName)
            )
        """
    };

    public MySqlDataAccess () throws DataAccessException{
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
            DatabaseManager.createDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
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
