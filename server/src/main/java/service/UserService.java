package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import request.RegisterRequest;
import result.RegisterResult;
import model.UserData;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;

import java.util.UUID;

public class UserService {

    DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {this.dataAccess = dataAccess;}

    public RegisterResult register(RegisterRequest request) throws DataAccessException, BadRequestException, AlreadyTakenException {
        if (request == null || request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (dataAccess.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        } else {
            dataAccess.createUser(new UserData(request.username(), request.password(), request.email()));
            String authToken = UUID.randomUUID().toString();
            dataAccess.createAuth(new AuthData(authToken, request.username()));
            return new RegisterResult(request.username(), authToken);
        }
    }
}
