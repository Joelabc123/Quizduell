package client;

import protocol.messages.LobbyStatusMessage;
import protocol.messages.LoginMessage;

public interface GameInterface {

    //SERVER -> CLIENT
    void loginMessage(LoginMessage loginMessage);

    //CLIENT -> SERVER
    void hostGame();

    void onLobbyStatusMessage(LobbyStatusMessage lobbyStatusMessage);
}
