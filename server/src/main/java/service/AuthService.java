package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import request.LogoutRequest;
import result.LogoutResult;
import service.exception.UnauthorizedException;

public class AuthService {

    DataAccess dataAccess;

    public AuthService(DataAccess dataAccess) {this.dataAccess = dataAccess;}

    public AuthData authenticate(String authToken) throws UnauthorizedException, DataAccessException {
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        } else {
            AuthData authData = dataAccess.getAuth(authToken);
            if (authData == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            return authData;
        }
    }
}
