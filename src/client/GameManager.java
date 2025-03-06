package client;

import protocol.Category;
import protocol.messages.*;
import server.Answer;
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

    public GameState getLatestGameState() {
        return latestGameState;
    }

    @Override
    public void loginMessage(LoginMessage loginMessage) {
        this.userId = loginMessage.getUserId();
        this.username = loginMessage.getUsername();

        System.out.println("Logged in as " + username);
    }

    @Override
    public void hostLobby() {
        this.clientHandler.sendMessage(new HostLobbyMessage());
    }

    @Override
    public void updateGameMessage(UpdateGameMessage UpdateGameMessage) {
        if(latestGameState == null) {
            this.latestGameState = UpdateGameMessage.getGameState();
            //Switch to waiting lobby

        } else {
            //Dummy
        }
    }

    @Override
    public void joinLobby(int lobbyCode) {
        this.clientHandler.sendMessage(new JoinLobbyMessage(lobbyCode));
    }

    @Override
    public void selectCategory(String categoryName) {
        Category category = latestGameState.getCategoryByName(categoryName);
        this.clientHandler.sendMessage(new SelectCategoryMessage(category));
    }

    @Override
    public void answerQuestion(Answer answer,String fId) {
        this.clientHandler.sendMessage(new AnswerQuestionMessage(answer,fId));
    }
}


