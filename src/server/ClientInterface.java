package server;

import protocol.messages.*;
import server.Answer;

public interface ClientInterface {

    //SERVER -> CLIENT
    void loginMessage(LoginMessage loginMessage);
    void startGameMessage(StartGameMessage startGameMessage);
    void sendCategoryMessage(SendCategoryMessage sendCategoryMessage);
    void sendQuestionMessage(SendQuestionMessage sendQuestionMessage);
    void updateGameMessage(UpdateGameMessage UpdateGameMessage);
    void gameOverMessage(GameOverMessage gameOverMessage);

    //CLIENT -> SERVER
    void hostLobby();
    void joinLobby(int lobbyCode);
    void selectCategory(String category);
    void answerQuestion(Answer answer);
}
