package server;

import protocol.Category;
import protocol.CategoryRound;
import protocol.GameOutcome;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    public UUID playerA, playerB;
    public String playerAName, playerBName;
    private final int lobbyCode;
    private boolean gameOver;
    private ArrayList<Category> availableCategories = new ArrayList<>();
    private ArrayList<CategoryRound> rounds = new ArrayList<>();
    private boolean turnPlayerA;
    private boolean turnPlayerB;
    private int turnQuestionRound;
    private int scorePlayerA;
    private int scorePlayerB;

    private final int MAX_ROUNDS = 6;
    private int currentRound;

    public GameState(ArrayList<Category> availableCategories) {
        this.lobbyCode = (int) (Math.random() * 9000) + 1000;
        this.availableCategories = new ArrayList<>(availableCategories);
        this.gameOver = false;
        this.turnQuestionRound = 0;
        this.scorePlayerA = 0;
        this.scorePlayerB = 0;
        this.turnPlayerA = true;
        this.turnPlayerB = false;
        this.currentRound = 0;
    }

    public GameState(GameState gameState) {
        this.playerA = gameState.playerA;
        this.playerB = gameState.playerB;
        this.playerAName = gameState.playerAName;
        this.playerBName = gameState.playerBName;
        this.lobbyCode = gameState.lobbyCode;
        // Tiefe Kopie der Rounds
        this.rounds = new ArrayList<>();
        for (CategoryRound cr : gameState.rounds) {
            this.rounds.add(new CategoryRound(cr));
        }
        this.gameOver = gameState.gameOver;
        this.availableCategories = new ArrayList<>(gameState.availableCategories);
        this.turnQuestionRound = gameState.turnQuestionRound;
        this.scorePlayerA = gameState.scorePlayerA;
        this.scorePlayerB = gameState.scorePlayerB;
        this.turnPlayerA = gameState.turnPlayerA;
        this.turnPlayerB = gameState.turnPlayerB;
        this.currentRound = gameState.currentRound;
    }

    // Getter und Setter (wie gehabt)
    public UUID getId() {
        return id;
    }

    public String getPlayerAName() {
        return playerAName;
    }

    public String getPlayerBName() {
        return playerBName;
    }

    public int getLobbyCode() {
        return lobbyCode;
    }

    public ArrayList<Category> getAvailableCategories() {
        return availableCategories;
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

    public void addRound(CategoryRound round) {
        rounds.add(round);
    }

    public CategoryRound getCurrentRound() {
        if (rounds.isEmpty()) {
            return null;
        }
        return rounds.getLast();
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

    public void incrementCurrentRound() {
        this.currentRound++;
    }

    public boolean isLastRound() {
        return currentRound >= MAX_ROUNDS;
    }

}
