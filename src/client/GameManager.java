package client;

import protocol.GameOutcome;
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

    @Override
    public void loginMessage(LoginMessage loginMessage) {
        this.userId = loginMessage.getUserId();
        this.username = loginMessage.getUsername();
        mainGameFrame.getLobbyStartPanel().setPlayerName(username);
        System.out.println("Logged in as " + username);
    }

    @Override
    public void hostLobby() {
        this.clientHandler.sendMessage(new HostLobbyMessage());
    }

    @Override
    public void updateGameMessage(UpdateGameMessage updateGameMessage) {
        this.latestGameState = updateGameMessage.getGameState();
        mainGameFrame.switchScorePanel();
        int scorePlayerA = latestGameState.getScorePlayerA();
        int scorePlayerB = latestGameState.getScorePlayerB();
        GameOutcome gameOutcome = (latestGameState.getCurrentRound() != null) ?
                latestGameState.getCurrentRound().getWinner() : null;

        System.out.println("ScorePlayerA: " + scorePlayerA);
        System.out.println("ScorePlayerB: " + scorePlayerB);
        System.out.println("GameOutcome: " + gameOutcome);

        mainGameFrame.getScorePanel().setScores(scorePlayerA, scorePlayerB);
        mainGameFrame.getScorePanel().addCategoryWinner(latestGameState.getCurrentRound().getCategory().getName(),latestGameState.getCurrentRound().getWinner());
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
    public void answerQuestion(ArrayList<Boolean> answers) {
        System.out.println("Sending Answers: " + answers);
        System.out.println("Sending Answers: " + answers.size());
        System.out.println("Sending Answers: " + answers.toString());
        this.clientHandler.sendMessage(new AnswerQuestionMessage(new ArrayList<>(answers)));
    }

    @Override
    public void startGameMessage(StartGameMessage startGameMessage) {
        this.latestGameState = startGameMessage.getGameState();
        mainGameFrame.switchScorePanel();
        mainGameFrame.getScorePanel().setLeftPlayerName(latestGameState.getPlayerAName());
        mainGameFrame.getScorePanel().setRightPlayerName(latestGameState.getPlayerBName());
        System.out.println("Setting Names" + latestGameState.getPlayerAName() + " " + latestGameState.getPlayerBName());
        if(latestGameState.playerA.equals(this.userId)) {
            mainGameFrame.getScorePanel().setLeftPlayerName(latestGameState.getPlayerAName() + " (Du)");
            mainGameFrame.getScorePanel().setRightPlayerName(latestGameState.getPlayerBName());

        } else if(latestGameState.playerB.equals(this.userId)) {
            mainGameFrame.getScorePanel().setLeftPlayerName(latestGameState.getPlayerAName());
            mainGameFrame.getScorePanel().setRightPlayerName(latestGameState.getPlayerBName() + " (Du)");
        }
    }

    @Override
    public void playerTurnMessage(PlayerTurnMessage playerTurnMessage) {
        this.latestGameState = playerTurnMessage.getGameState();
        System.out.println("PlayerTurnMessage: ");
        if(latestGameState.playerA.equals(this.userId)) {
            System.out.println("Player A" + latestGameState.getTurnPlayerA());
            mainGameFrame.getScorePanel().setChooseCategoryButtonVisible(playerTurnMessage.getGameState().getTurnPlayerA());

        } else if(latestGameState.playerB.equals(this.userId)) {
            System.out.println("Player B" + latestGameState.getTurnPlayerB());
            mainGameFrame.getScorePanel().setChooseCategoryButtonVisible(playerTurnMessage.getGameState().getTurnPlayerB());
        }
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
        mainGameFrame.switchQuestionPanel();
        setQuestions();
    }

    @Override
    public void gameOverMessage(GameOverMessage gameOverMessage) {
        this.latestGameState = gameOverMessage.getGameState();
        mainGameFrame.switchStatisticsPanel();
        mainGameFrame.getStatisticsPanel().setFinalStatistics(latestGameState.getScorePlayerA(), latestGameState.getScorePlayerB(),latestGameState.playerAName, latestGameState.playerBName);
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
        System.out.println("Setting Questions for Category: " + kat);
        Question randomQuestion;
        // Wähle eine zufällige Frage aus der Liste und entferne sie aus der Liste
        switch(latestGameState.getTurnQuestionRound()) {
            case 0:
                randomQuestion = latestGameState.getCurrentRound().getQuestions().getFirst();
                latestGameState.setTurnQuestionRound(1);
                break;
            case 1:
                randomQuestion = latestGameState.getCurrentRound().getQuestions().get(1);
                latestGameState.setTurnQuestionRound(2);
                break;
            case 2:
                randomQuestion = latestGameState.getCurrentRound().getQuestions().get(2);
                break;
            default:
                System.out.println("Keine Frage gefunden");
                randomQuestion = null;
        }
        String randomQuestionText = randomQuestion.getFrageName();
        System.out.println("RandomQuestionText: " + randomQuestionText);
        ArrayList<String> answerTexts = randomQuestion.getAnswerOptions();
        System.out.println("AnswerTexts: " + answerTexts);
        Answer answer = randomQuestion.getCorrectAnswer();
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
