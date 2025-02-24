package server;

import java.util.ArrayList;
import java.util.Date;

public class QuizGame implements Game, Runnable {

    private Player playerA, playerB;
    private GameState gameState = null;
    private int size;

    private ArrayList<Ship> ships = new ArrayList<>();

    @Override
    public void run() {
        //TODO MORGEN TAKT IMPLEMENTIEREN

    }

    @Override
    public GameState createGame(int size) {
        this.gameState = new GameState(size, ships);

        return gameState;
    }

    @Override
    public GameState addPlayer(Player player) {
        return null;
    }

    @Override
    public GameState leaveGame(Player player) {
        return null;
    }


    @Override
    public GameState addPlayer(PlayerInfo player) {
        if(!this.gameState.getStatus().equals(GameState.GameStatus.LOBBY_WAITING)) {
            return null;
        }

        if (playerA == null) {
            playerA = player;
            playerA.sendMessage(new GameStateUpdateMessage(gameState));
        } else if (playerB == null) {
            playerB = player;
            playerB.sendMessage(new GameStateUpdateMessage(gameState));
        }

        if(playerA != null && playerB != null) {
            gameState.setStatus(GameState.GameStatus.BUILD_GAME_BOARD);

            Date date = new Date();

            GameState gameState = new GameState(this.getGameState());

            gameState.setBuildGameBoardStarted(date);
            gameState.setBuildGameBoardFinished(new Date(date.getTime() + Parameters.SHOOT_TIME_IN_SECONDS * 1000));

            this.gameState = gameState;

            playerA.sendMessage(new GameStartingMessage(gameState));
            playerB.sendMessage(new GameStartingMessage(gameState));

            System.out.println("Game started");
        }
        return null;
    }

    @Override
    public GameState leaveGame(PlayerInfo player) {

        return gameState;
    }

    private synchronized void startBuildPhase() {

    }


    public synchronized void sendGameStateUpdate() {

    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getSize() {
        return size;
    }
}