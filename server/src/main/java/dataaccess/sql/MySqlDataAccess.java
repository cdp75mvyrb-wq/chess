package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySqlDataAccess implements DataAccess {
    private final String[] createStatements = {
        """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameID)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )
        """
    };

    private final Gson gson = new Gson();

    public MySqlDataAccess () throws DataAccessException{
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
;        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to configure database", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to get user", e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement("SELECT authToken, username FROM auth WHERE authToken = ?")) {
            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString(1), rs.getString(2));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to get authentication", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM game WHERE gameID = ?")) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), gson.fromJson(rs.getString("game"), ChessGame.class));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to get game", e);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM game")) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    games.add(new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), gson.fromJson(rs.getString(5), ChessGame.class)));
                }
                return games;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unable to get game", e);
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        executeUpdate("UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?" , "Error: unable to update game data", gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gson.toJson(gameData.game()), gameData.gameID());
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        executeUpdate("INSERT INTO user (username, password, email) VALUES (?, ?, ?)", "Error: unable to add user data", userData.username(), userData.password(), userData.email());
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        executeUpdate("INSERT INTO auth (authToken, username) VALUES (?, ?)", "Error: unable to add authentication data", authData.authToken(), authData.username());
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        executeUpdate("DELETE FROM auth WHERE authToken = ?", "Error: unable to delete auth data", authToken);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String gameJson = gson.toJson(new ChessGame());
        return executeUpdate("INSERT INTO game (gameName, game) VALUES (?, ?)", "Error: unable to create game", gameName, gameJson);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE user", "Error: unable to clear users data");
    }

    @Override
    public void clearAuthTokens() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE auth", "Error: unable to clear auth data");
    }

    @Override
    public void clearGames() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE game", "Error: unable to clear games data");
    }

    private int executeUpdate(String statement, String message, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(message, e);
        }
    }
}
