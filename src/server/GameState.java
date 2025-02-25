package server;


import protocol.CategoryRound;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class GameState implements Serializable  {

    private static final long serialVersionUID = 1L;

    public enum GameStatus {
        LOBBY_WAITING,
        IN_GAME,
        GAME_OVER
    }

    private final UUID id = UUID.randomUUID();

    public UUID playerA, playerB;
    private int sessionCode;

    private ArrayList<CategoryRound> rounds = new ArrayList<>();

    private GameStatus status = GameStatus.LOBBY_WAITING;

    public GameState(GameState gameState) {
        this.playerA = gameState.playerA;
        this.playerB = gameState.playerB;
        this.status = gameState.status;
    }

    public GameState(UUID playerA, UUID playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public GameState addPlayer(UUID player) {
        if(playerA == null) {
            playerA = player;

        } else if(playerB == null) {
            playerB = player;
        }
        return this;
    }

    public GameState removePlayer(UUID player) {
        if(playerA.equals(player)) {
            playerA = null;
        } else if(playerB.equals(player)) {
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
}