package server;

import protocol.*;

import protocol.messages.*;

import java.util.*;

public class QuizGame implements Game, Runnable {

    private ArrayList<Category> availableCategories = new ArrayList<>();

    private Player playerA, playerB;
    private GameState gameState = null;

    public QuizGame(ArrayList<Category> categories) {
        this.gameState = new GameState(Server.quizReader.getEmptyCategories());
        this.availableCategories = categories;
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

            System.out.println(playerA.getUsername() + " " + playerB.getUsername());
            System.out.println(playerA.getId() + " " + playerB.getId());

            gameState.addPlayer(playerA.getId(), playerA.getUsername());
            gameState.addPlayer(playerB.getId(), playerB.getUsername());

            this.gameState = gameState;


            System.out.println("turnPlayerA: " + gameState.getTurnPlayerA());
            System.out.println("turnPlayerB: " + gameState.getTurnPlayerB());

            System.out.println("added players");
            playerA.sendMessage(new StartGameMessage(gameState));
            playerB.sendMessage(new StartGameMessage(gameState));

            GameState gameState2 = new GameState(this.getGameState());

            playerA.sendMessage(new PlayerTurnMessage(gameState2));
            playerB.sendMessage(new PlayerTurnMessage(gameState2));

            this.gameState = gameState2;
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

    public ArrayList<Category> getAvailableCategories() {
        return availableCategories;
    }
}