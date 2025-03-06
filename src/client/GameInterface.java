package client;

import protocol.messages.*;
import server.Answer;

public interface GameInterface {

    //SERVER -> CLIENT
    void loginMessage(LoginMessage loginMessage);
    void updateGameMessage(UpdateGameMessage UpdateGameMessage);

    //CLIENT -> SERVER
    void hostLobby();
    void joinLobby(int lobbyCode);
    void selectCategory(String category);
    void answerQuestion(Answer answer,String fId);
}
