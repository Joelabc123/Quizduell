package client;

import protocol.messages.LobbyStatusMessage;
import protocol.messages.LoginMessage;

import java.util.UUID;

public interface GameInterface {

    //SERVER -> CLIENT
    void loginMessage(LoginMessage loginMessage);

    //CLIENT -> SERVER
    void hostLobby();

    //CLIENT -> SERVER
    void joinLobby(UUID playerId, int lobbyCode);

    //SERVER -> CLIENT
    void lobbyStatus(LobbyStatusMessage lobbyStatusMessage);

    //SERVER -> CLIENT
    void startGame();
}
