package server;

import protocol.Category;
import protocol.CategoryRound;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    // Enum für den Spielstatus
    public enum GameStatus {
        LOBBY_WAITING,
        IN_GAME,
        GAME_OVER
    }

    // Instanzfelder
    private final UUID id = UUID.randomUUID();
    public UUID playerA, playerB;
    public String playerAName, playerBName;
    private final int lobbyCode;
    private boolean gameOver;
    private ArrayList<Category> availableCategories = new ArrayList<>();
    private ArrayList<CategoryRound> rounds = new ArrayList<>();
    private GameStatus status = GameStatus.LOBBY_WAITING;
    private Date selectCategoryStarted, selectCategoryFinished;
    private boolean playerTurn;

    // Konstruktor: Initialisierung des GameState mit verfügbaren Kategorien
    public GameState(ArrayList<Category> availableCategories) {
        this.lobbyCode = (int) (Math.random() * 9000) + 1000;
        this.availableCategories = new ArrayList<>(availableCategories);
        this.gameOver = false;
        this.playerTurn = false;
    }

    // Kopierkonstruktor: Erstellt tiefe Kopien der Listen und Datumseinträge
    public GameState(GameState gameState) {
        this.playerA = gameState.playerA;
        this.playerB = gameState.playerB;
        this.playerAName = gameState.playerAName;
        this.playerBName = gameState.playerBName;
        this.status = gameState.status;
        this.lobbyCode = gameState.lobbyCode;
        // Tiefe Kopie der Listen
        this.rounds = new ArrayList<>(gameState.rounds);
        System.out.println("rounds: " + rounds);
        this.availableCategories = new ArrayList<>(gameState.availableCategories);
        this.gameOver = gameState.gameOver;
        // Datumseinträge als Kopien
        this.selectCategoryStarted = (gameState.selectCategoryStarted != null) ? new Date(gameState.selectCategoryStarted.getTime()) : null;
        this.selectCategoryFinished = (gameState.selectCategoryFinished != null) ? new Date(gameState.selectCategoryFinished.getTime()) : null;
        this.playerTurn = gameState.playerTurn;
    }

    // Getter und Setter

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
        this.rounds = new ArrayList<>(rounds);
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

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    // Weitere Methoden zur Spielzustandsverwaltung

    public GameState addPlayer(UUID player, String username) {
        if (playerA == null) {
            playerA = player;
            playerAName = username;
        } else if (playerB == null) {
            playerB = player;
            playerBName = username;
        }
        return this;
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
}
