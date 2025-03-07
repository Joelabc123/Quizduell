package client;

import protocol.Category;
import protocol.messages.*;
import server.Answer;
import server.GameState;
import client.gui.MainGameFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GameManager implements GameInterface {

    private UUID userId;
    private String username;

    private GameState latestGameState;
    private MainGameFrame mainGameFrame; // Neuer Verweis

    private final ClientHandler clientHandler;

    public GameManager(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    // Neuer Setter, um den MainGameFrame zu setzen
    public void setMainGameFrame(MainGameFrame mainGameFrame) {
        this.mainGameFrame = mainGameFrame;
    }

    // Neuer Getter, falls benötigt
    public MainGameFrame getMainGameFrame() {
        return mainGameFrame;
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
    public void updateGameMessage(UpdateGameMessage updateGameMessage) {
        this.latestGameState = updateGameMessage.getGameState();
        // Zusätzliche Logik – z. B. MainGameFrame über update informieren
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
    public void answerQuestion(Answer answer, String fId) {
        this.clientHandler.sendMessage(new AnswerQuestionMessage(answer, fId));
    }

    @Override
    public void startGameMessage(StartGameMessage startGameMessage) {
        this.latestGameState = startGameMessage.getGameState();
        mainGameFrame.switchScorePanel();
        mainGameFrame.getScorePanel().setLeftPlayerName(latestGameState.getPlayerAName());
        mainGameFrame.getScorePanel().setRightPlayerName(latestGameState.getPlayerBName());
        System.out.println("Setting Names" + latestGameState.getPlayerAName() + " " + latestGameState.getPlayerBName());
    }

    @Override
    public void playerTurnMessage(PlayerTurnMessage playerTurnMessage) {
        mainGameFrame.getScorePanel().setChooseCategoryButtonVisible(playerTurnMessage.getPlayerTurn());
        if(playerTurnMessage.getPlayerTurn()){
            mainGameFrame.switchCategoryWheelPanel();
            ArrayList<Category> catList = new ArrayList<>(latestGameState.getAvailableCategories());
            Collections.shuffle(catList);
            String[] randomCategories = catList.stream()
                    .limit(3)
                    .map(Category::getName)
                    .toArray(String[]::new);

            mainGameFrame.getCategoryWheelPanel().setCategories(randomCategories);
        }
    }

    @Override
    public void hostedLobbyMessage(HostedLobbyMessage hostedLobbyMessage) {
        this.latestGameState = hostedLobbyMessage.getGameState();
        System.out.println("Gamestate GameManager: " + hostedLobbyMessage.getGameState().getLobbyCode());
        mainGameFrame.getLobbyHostPanel().setLobbyId(String.valueOf(latestGameState.getLobbyCode()));
    }

    @Override
    public void authenticationError() {
        mainGameFrame.getLobbyJoinPanel().showAuthenticationError();
    }
}
