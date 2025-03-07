package client;

import protocol.messages.*;
import server.Answer;
public interface GameInterface {

    //SERVER -> CLIENT
    void loginMessage(LoginMessage loginMessage);
    void updateGameMessage(UpdateGameMessage UpdateGameMessage);
    void startGameMessage(StartGameMessage startGameMessage);
    void hostedLobbyMessage(HostedLobbyMessage hostedLobbyMessage);
    void playerTurnMessage(PlayerTurnMessage playerTurnMessage);

    //CLIENT -> SERVER
    void hostLobby();
    void joinLobby(int lobbyCode);
    void selectCategory(String category);
    void answerQuestion(Answer answer,String fId);

    //FEHLER
    void authenticationError();
}
