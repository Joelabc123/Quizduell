package client;

import protocol.messages.*;
import server.Answer;
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
        this.clientHandler.sendMessage(new HostLobbyMessage());
    }


    @Override
    public void sendCategoryMessage(SendCategoryMessage sendCategoryMessage) {

    }

    @Override
    public void sendQuestionMessage(SendQuestionMessage sendQuestionMessage) {

    }

    @Override
    public void updateGameMessage(UpdateGameMessage UpdateGameMessage) {
        this.latestGameState = UpdateGameMessage.getGameState();
        this.lobbyCode = latestGameState.getLobbyCode();
    }

    @Override
    public void joinLobby(int lobbyCode) {
        this.clientHandler.sendMessage(new JoinLobbyMessage(lobbyCode));
    }

    @Override
    public void selectCategory(String category) {

    }

    @Override
    public void answerQuestion(Answer answer) {

    }
}


