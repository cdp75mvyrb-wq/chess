package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class ClearService {

    DataAccess dataAccess;

    public ClearService(DataAccess dataAccess) {this.dataAccess = dataAccess;}

    public void clear() throws DataAccessException {
        dataAccess.clearUsers();
        dataAccess.clearAuthTokens();
        dataAccess.clearGames();
    }
}
