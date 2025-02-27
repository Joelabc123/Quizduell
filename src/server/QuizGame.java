package server;

import protocol.messages.UpdateGameMessage;

import java.util.Date;

public class QuizGame implements Game, Runnable {

    private Player playerA, playerB;
    private GameState gameState = null;

    public QuizGame() {
        this.gameState = new GameState(Server.quizReader.getEmptyCategories());
    }

    @Override
    public void run() {
        //TODO MORGEN TAKT IMPLEMENTIEREN

    }

    @Override
    public GameState addPlayer(Player player) {
        if (!this.gameState.getStatus().equals(GameState.GameStatus.LOBBY_WAITING)) {
            return null;
        }

        if (playerA == null) {
            playerA = player;
            playerA.sendMessage(new UpdateGameMessage(gameState));
        } else if (playerB == null) {
            playerB = player;
            playerB.sendMessage(new UpdateGameMessage(gameState));
        }

        if (playerA != null && playerB != null) {
            gameState.setStatus(GameState.GameStatus.IN_GAME);

            //Time to select Category
            Date date = new Date();

            GameState gameState = new GameState(this.getGameState());

            gameState.setSelectCategoryStarted(date);
            gameState.setSelectCategoryFinished(new Date(date.getTime() + 10 * 1000));

            this.gameState = gameState;

            playerA.sendMessage(new UpdateGameMessage(gameState));
        }
        return null;
    }

    @Override
    public GameState leaveGame(Player player) {

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

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }
}