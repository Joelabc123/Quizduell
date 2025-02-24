package client;

import protocol.messages.HostLobbyMessage;
import protocol.messages.LobbyStatusMessage;
import protocol.messages.LoginMessage;
import server.GameState;

import java.util.UUID;

public class GameManager implements GameInterface {

    private UUID userId;
    private String username;

    private GameState latestGameState;

    private final ClientHandler clientHandler;

    public GameManager(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void loginMessage(LoginMessage loginMessage) {
        this.userId = loginMessage.getUserId();
        this.username = loginMessage.getUsername();
    }

    @Override
    public void hostGame() {
        this.clientHandler.sendMessage(new HostLobbyMessage());
    }

    @Override
    public void onLobbyStatusMessage(LobbyStatusMessage lobbyStatusMessage) {
        System.out.println("Game state: " + gameState);
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
