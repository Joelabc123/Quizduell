package server;

import protocol.*;

import protocol.messages.*;

import java.util.*;

public class QuizGame implements Game, Runnable {

    private ArrayList<Category> aviabaleCategories = new ArrayList<>();

    private Player playerA, playerB;
    private GameState gameState = null;

    public QuizGame(ArrayList<Category> categories) {
        this.gameState = new GameState(Server.quizReader.getEmptyCategories());
        this.aviabaleCategories = categories;
    }

    public void run() {

    }

    public GameState addPlayer(Player player) {
        System.out.println("Adding player to game");
        if (!this.gameState.getStatus().equals(GameState.GameStatus.LOBBY_WAITING)) {
            return null;
        }

        if (playerA == null) {
            playerA = player;
            System.out.println("Player A: " + playerA.getUsername());
            System.out.println("gameState: " + gameState.getLobbyCode());
            playerA.sendMessage(new HostedLobbyMessage(gameState));
        } else if (playerB == null) {
            System.out.println("Player B: " + player.getUsername());
            playerB = player;
        }

        if (playerA != null && playerB != null) {
            gameState.setStatus(GameState.GameStatus.IN_GAME);

            //Time to select Category
            Date date = new Date();

            GameState gameState = new GameState(this.getGameState());

            gameState.setSelectCategoryStarted(date);
            gameState.setSelectCategoryFinished(new Date(date.getTime() + 10 * 1000));

            gameState.addPlayer(playerA.getId(), playerA.getUsername());
            gameState.addPlayer(playerB.getId(), playerB.getUsername());

            this.gameState = gameState;

            System.out.println("added players");
            playerA.sendMessage(new StartGameMessage(gameState));
            playerB.sendMessage(new StartGameMessage(gameState));


            PlayerTurnMessage playerTurnMessage = new PlayerTurnMessage();
            if(gameState.isPlayerTurn()){
                playerTurnMessage.setPlayerTurn(true);
                playerA.sendMessage(playerTurnMessage);

                playerTurnMessage.setPlayerTurn(false);
                playerB.sendMessage(new PlayerTurnMessage());
            } else {
                playerTurnMessage.setPlayerTurn(true);
                playerB.sendMessage(playerTurnMessage);

                playerTurnMessage.setPlayerTurn(false);
                playerA.sendMessage(new PlayerTurnMessage());
            }
        }
        return null;
    }

    public void broadcast(Message msg) {
        if (playerA != null) {
            playerA.sendMessage(msg);
        }
        if (playerB != null) {
            playerB.sendMessage(msg);
        }
    }

    public GameState leaveGame(Player player) {

        return gameState;
    }

    private synchronized void startBuildPhase() {

    }


    public synchronized void sendGameStateUpdate() {

    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public ArrayList<Category> getAviabaleCategories() {
        return aviabaleCategories;
    }
}