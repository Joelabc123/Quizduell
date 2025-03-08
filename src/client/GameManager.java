package client;

import protocol.Category;
import protocol.Question;
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
    }

    @Override
    public void hostedLobbyMessage(HostedLobbyMessage hostedLobbyMessage) {
        this.latestGameState = hostedLobbyMessage.getGameState();
        System.out.println("Gamestate GameManager: " + hostedLobbyMessage.getGameState().getLobbyCode());
        mainGameFrame.getLobbyHostPanel().setLobbyId(String.valueOf(latestGameState.getLobbyCode()));
    }

    @Override
    public void sendCategoriesMessage(SendCategoriesMessage sendCategoriesMessage) {
        this.latestGameState = sendCategoriesMessage.getGameState();
        setQuestions();
    }

    //HELPER
    @Override
    public void authenticationError() {
        mainGameFrame.getLobbyJoinPanel().showAuthenticationError();
    }

    public void setCategories(){
        mainGameFrame.switchCategoryWheelPanel();
        ArrayList<Category> catList = new ArrayList<>(latestGameState.getAvailableCategories());
        Collections.shuffle(catList);
        String[] randomCategories = catList.stream()
                .limit(3)
                .map(Category::getName)
                .toArray(String[]::new);

        mainGameFrame.getCategoryWheelPanel().setCategories(randomCategories);
    }

    public void setQuestions(){
        String kat = latestGameState.getCurrentRound().getCategory().getName();
        // Wähle eine zufällige Frage aus der Liste und entferne sie aus der Liste
        Question randomQuestion = latestGameState.getCurrentRound().getCategory().getQuestions()
                .remove((int)(Math.random() * latestGameState.getCurrentRound().getCategory().getQuestions().size()));
        String randomQuestionText = randomQuestion.getFrageName();
        ArrayList<String> answerTexts = randomQuestion.getAnswerOptions();
        System.out.println("AnswerTexts: " + answerTexts);
        Answer answer = randomQuestion.getCorrectAnswer();

        mainGameFrame.switchQuestionPanel();
        mainGameFrame.getQuestionPanel().setCategory(kat);
        mainGameFrame.getQuestionPanel().setQuestionText(randomQuestionText);
        mainGameFrame.getQuestionPanel().setCorrectAnswer(answer);
        // Übergibt alle vier Antwortmöglichkeiten an die QuestionPanel-Methode
        mainGameFrame.getQuestionPanel().setAnswerOptions(
                answerTexts.get(0),
                answerTexts.get(1),
                answerTexts.get(2),
                answerTexts.get(3)
        );
    }

}
