package server;

import protocol.Category;
import protocol.CategoryRound;

import java.io.Serializable;
import java.lang.reflect.Array;
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
    private final int lobbyCode;

    private ArrayList<Category> availableCategories = new ArrayList<>();
    private ArrayList<CategoryRound> rounds = new ArrayList<>();

    private GameStatus status = GameStatus.LOBBY_WAITING;

    private UUID playerTurn;
    private Date selectCategoryStarted, selectCategoryFinished;

    public GameState(ArrayList<Category> availableCategories) {
        this.lobbyCode = (int) (Math.random() * 9000) + 1000;
        this.availableCategories = availableCategories;
    }

    public GameState(GameState gameState) {
        this.playerA = gameState.playerA;
        this.playerB = gameState.playerB;
        this.status = gameState.status;
        this.lobbyCode = gameState.lobbyCode;
        this.rounds = gameState.rounds;
        this.playerTurn = gameState.playerTurn;
    }

    public GameState addPlayer(UUID player) {
        if (playerA == null) {
            playerA = player;

        } else if (playerB == null) {
            playerB = player;
        }
        return this;
    }

    public GameState removePlayer(UUID player) {
        if (playerA.equals(player)) {
            playerA = null;
        } else if (playerB.equals(player)) {
            playerB = null;
        }
        return this;
    }

    public int getPlayerCount() {
        return (playerA != null ? 1 : 0) + (playerB != null ? 1 : 0);
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public UUID getId() {
        return id;
    }

    public int getLobbyCode() {
        return lobbyCode;
    }

    public ArrayList<CategoryRound> getRounds() {
        return rounds;
    }

    public UUID getPlayerTurn() {
        return playerTurn;
    }

    public void addRound(CategoryRound round) {
        rounds.add(round);
    }

    public CategoryRound getCurrentRound() {
        return rounds.getLast();
    }
}