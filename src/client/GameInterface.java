package client;

import protocol.messages.*;

import java.util.ArrayList;

public interface GameInterface {

    //SERVER -> CLIENT
    void loginMessage(LoginMessage loginMessage);
    void updateGameMessage(UpdateGameMessage UpdateGameMessage);
    void startGameMessage(StartGameMessage startGameMessage);
    void hostedLobbyMessage(HostedLobbyMessage hostedLobbyMessage);
    void playerTurnMessage(PlayerTurnMessage playerTurnMessage);
    void sendCategoriesMessage(SendCategoriesMessage sendCategoriesMessage);
    void gameOverMessage(GameOverMessage gameOverMessage);

    //CLIENT -> SERVER
    void hostLobby();
    void joinLobby(int lobbyCode);
    void selectCategory(String category);
    void answerQuestion(ArrayList<Boolean> answers);

    //FEHLER
    void authenticationError();
}
