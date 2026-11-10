package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;

public class ServerFacade {
    public final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final String serverURL;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public void clear() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/db"))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        String json = gson.toJson(registerRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
        return gson.fromJson(response.body(), RegisterResult.class);
    }

    public LoginResult login(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String json = gson.toJson(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/session"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
        return gson.fromJson(response.body(), LoginResult.class);
    }

    public LogoutResult logout(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/session"))
                .header("authorization", authToken)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
        return new LogoutResult();
    }

    public ListGamesResult listGames(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/game"))
                .header("authorization", authToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
        return gson.fromJson(response.body(), ListGamesResult.class);
    }

    public CreateGameResult createGame(String authToken, String gameName) throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        String json = gson.toJson(createGameRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/game"))
                .header("authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
        return gson.fromJson(response.body(), CreateGameResult.class);
    }

    public JoinGameResult joinGame(String authToken, String color, int gameID) throws Exception {
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID);
        String json = gson.toJson(joinGameRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + "/game"))
                .header("authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SuccessCheck(response);
        return gson.fromJson(response.body(), JoinGameResult.class);
    }

    private void SuccessCheck(HttpResponse<String> response) throws Exception {
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }
}
