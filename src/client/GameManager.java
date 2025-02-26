package client;

import protocol.messages.HostLobbyMessage;
import protocol.messages.JoinLobbyMessage;
import protocol.messages.LobbyStatusMessage;
import protocol.messages.LoginMessage;
import server.GameState;

import java.util.UUID;

public class GameManager implements GameInterface {

    private UUID userId;
    private String username;
    private int lobbyCode;

    private GameState latestGameState;

    private final ClientHandler clientHandler;

    public GameManager(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void loginMessage(LoginMessage loginMessage) {
        this.userId = loginMessage.getUserId();
        this.username = loginMessage.getUsername();

        //Über stagemanager scene wechseln
    }

    @Override
    public void hostLobby() {
        this.clientHandler.sendMessage(new HostLobbyMessage(userId));
    }

    @Override
    public void lobbyStatus(LobbyStatusMessage lobbyStatusMessage) {
        this.lobbyCode = lobbyStatusMessage.getLobbyCode();
        //Über stagemanager scene wechseln
    }

    @Override
    public void joinLobby(UUID playerId,int lobbyCode) {
        this.clientHandler.sendMessage(new JoinLobbyMessage(userId, lobbyCode));
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}


