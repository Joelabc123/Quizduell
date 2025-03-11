package server;

import protocol.Category;
import protocol.CategoryRound;
import protocol.GameOutcome;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum GameStatus {
        LOBBY_WAITING,
        IN_GAME,
        GAME_OVER
    }

    private final UUID id = UUID.randomUUID();
    public UUID playerA, playerB;
    public String playerAName, playerBName;
    private final int lobbyCode;
    private boolean gameOver;
    private ArrayList<Category> availableCategories = new ArrayList<>();
    private ArrayList<CategoryRound> rounds = new ArrayList<>();
    private GameStatus status = GameStatus.LOBBY_WAITING;
    private Date selectCategoryStarted, selectCategoryFinished;
    private boolean turnPlayerA;
    private boolean turnPlayerB;
    private int turnQuestionRound;
    private int scorePlayerA;
    private int scorePlayerB;

    public GameState(ArrayList<Category> availableCategories) {
        this.lobbyCode = (int) (Math.random() * 9000) + 1000;
        this.availableCategories = new ArrayList<>(availableCategories);
        this.gameOver = false;
        this.turnQuestionRound = 0;
        this.scorePlayerA = 0;
        this.scorePlayerB = 0;
        this.turnPlayerA = true;
        this.turnPlayerB = false;
    }

    public GameState(GameState gameState) {
        this.playerA = gameState.playerA;
        this.playerB = gameState.playerB;
        this.playerAName = gameState.playerAName;
        this.playerBName = gameState.playerBName;
        this.status = gameState.status;
        this.lobbyCode = gameState.lobbyCode;
        // Tiefe Kopie der Rounds
        this.rounds = new ArrayList<>();
        for (CategoryRound cr : gameState.rounds) {
            this.rounds.add(new CategoryRound(cr));
        }
        this.gameOver = gameState.gameOver;
        this.availableCategories = new ArrayList<>(gameState.availableCategories);
        this.selectCategoryStarted = (gameState.selectCategoryStarted != null) ? new Date(gameState.selectCategoryStarted.getTime()) : null;
        this.selectCategoryFinished = (gameState.selectCategoryFinished != null) ? new Date(gameState.selectCategoryFinished.getTime()) : null;
        this.turnQuestionRound = gameState.turnQuestionRound;
        this.scorePlayerA = gameState.scorePlayerA;
        this.scorePlayerB = gameState.scorePlayerB;
        this.turnPlayerA = gameState.turnPlayerA;
        this.turnPlayerB = gameState.turnPlayerB;
    }

    // Getter und Setter (wie gehabt)
    public UUID getId() {
        return id;
    }

    public UUID getPlayerA() {
        return playerA;
    }

    public void setPlayerA(UUID playerA) {
        this.playerA = playerA;
    }

    public UUID getPlayerB() {
        return playerB;
    }

    public void setPlayerB(UUID playerB) {
        this.playerB = playerB;
    }

    public String getPlayerAName() {
        return playerAName;
    }

    public void setPlayerAName(String playerAName) {
        this.playerAName = playerAName;
    }

    public String getPlayerBName() {
        return playerBName;
    }

    public void setPlayerBName(String playerBName) {
        this.playerBName = playerBName;
    }

    public int getLobbyCode() {
        return lobbyCode;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public ArrayList<Category> getAvailableCategories() {
        return availableCategories;
    }

    public void setAvailableCategories(ArrayList<Category> availableCategories) {
        this.availableCategories = new ArrayList<>(availableCategories);
    }

    public ArrayList<CategoryRound> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<CategoryRound> rounds) {
        this.rounds = new ArrayList<>();
        for (CategoryRound cr : rounds) {
            this.rounds.add(new CategoryRound(cr));
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Date getSelectCategoryStarted() {
        return (selectCategoryStarted != null) ? new Date(selectCategoryStarted.getTime()) : null;
    }

    public void setSelectCategoryStarted(Date selectCategoryStarted) {
        this.selectCategoryStarted = (selectCategoryStarted != null) ? new Date(selectCategoryStarted.getTime()) : null;
    }

    public Date getSelectCategoryFinished() {
        return (selectCategoryFinished != null) ? new Date(selectCategoryFinished.getTime()) : null;
    }

    public void setSelectCategoryFinished(Date selectCategoryFinished) {
        this.selectCategoryFinished = (selectCategoryFinished != null) ? new Date(selectCategoryFinished.getTime()) : null;
    }

    public boolean getTurnPlayerA() {
        return turnPlayerA;
    }

    public boolean getTurnPlayerB() {
        return turnPlayerB;
    }

    public int getTurnQuestionRound() {
        return turnQuestionRound;
    }

    public void setTurnQuestionRound(int turnQuestionRound) {
        this.turnQuestionRound = turnQuestionRound;
    }

    public int getScorePlayerA() {
        return scorePlayerA;
    }

    public int getScorePlayerB() {
        return scorePlayerB;
    }

    public void switchPlayerTurn() {
        if(turnPlayerA) {
            setTurnPlayerA(false);
            setTurnPlayerB(true);

        } else {
            setTurnPlayerA(true);
            setTurnPlayerB(false);
        }
    }

    public void addPlayer(UUID player, String username) {
        if (playerA == null) {
            playerA = player;
            playerAName = username;
        } else if (playerB == null) {
            playerB = player;
            playerBName = username;
        }
    }

    public GameState removePlayer(UUID player) {
        if (playerA != null && playerA.equals(player)) {
            playerA = null;
        } else if (playerB != null && playerB.equals(player)) {
            playerB = null;
        }
        return this;
    }

    public int getPlayerCount() {
        return (playerA != null ? 1 : 0) + (playerB != null ? 1 : 0);
    }

    public void addRound(CategoryRound round) {
        rounds.add(round);
    }

    public CategoryRound getCurrentRound() {
        if (rounds.isEmpty()) {
            return null;
        }
        return rounds.get(rounds.size() - 1);
    }

    public Category getCategoryByName(String categoryName) {
        for (Category category : availableCategories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    public void updateScores(){
        int scoreA = 0;
        int scoreB = 0;
        for (CategoryRound round : rounds) {
            if (round.getWinner() == GameOutcome.PLAYER_A) {
                scoreA++;
            } else if (round.getWinner() == GameOutcome.PLAYER_B) {
                scoreB++;
            }
        }
        scorePlayerA = scoreA;
        scorePlayerB = scoreB;
    }

    public void setTurnPlayerA(boolean turnPlayerA) {
        this.turnPlayerA = turnPlayerA;
    }

    public void setTurnPlayerB(boolean turnPlayerB) {
        this.turnPlayerB = turnPlayerB;
    }
}
