package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Server {

    public static QuizReader quizReader;

    private static Server instance;

    private static int PORT = 12345;
    private ServerSocket socketServer;
    private boolean isRunning = false;
    private final List<Player> connectedPlayers = new ArrayList<>();
    private final List<Thread> threadClients = new ArrayList<>();

    // Map, die das Spiel und den zugehörigen Thread speichert
    private final Map<UUID, SpielContainer> spielMap = new HashMap<>();

    // Innere Record-Klasse, die QuizGame und den zugehörigen Thread zusammenhält
    private record SpielContainer(QuizGame spiel, Thread laufenderThread) {
    }

    public Server() {
        instance = this;
    }

    public void startServer() {
        if (isRunning) return;

        new Thread(() -> {
            try {
                socketServer = new ServerSocket(PORT);
                isRunning = true;
                System.out.println("Server gestartet auf Port " + PORT);

                while (isRunning) {
                    Socket clientSock = socketServer.accept();
                    System.out.println("Neuer Client verbunden: " + clientSock);

                    Player spieler = new Player(clientSock, this);
                    connectedPlayers.add(spieler);

                    Thread clientRunnableThread = new Thread(spieler);
                    threadClients.add(clientRunnableThread);
                    clientRunnableThread.start();
                }
            } catch (IOException ex) {
                if (isRunning) ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Registers a new game. Der Server erstellt intern den Game-Thread und startet ihn.
     * @param game the game to register
     */
    public void registerGame(QuizGame game) {
        Thread gameLaufThread = new Thread(game);
        SpielContainer container = new SpielContainer(game, gameLaufThread);
        spielMap.put(game.getGameState().getId(), container);
        gameLaufThread.start();
    }

    /**
     * Unregisters a game with the given id by interrupting its thread and removing it from the map.
     * @param id id of the game to remove
     */
    public void unregisterGame(UUID id) {
        SpielContainer container = spielMap.get(id);
        if (container != null) {
            container.laufenderThread().interrupt();
            spielMap.remove(id);
            System.out.println("Game " + id + " removed");
        }
    }

    /**
     * Returns the game the player is currently in.
     * @param player the player for whom to retrieve the game
     * @return the game the player is in or null if none
     */
    public synchronized QuizGame getGame(Player player) {
        for (SpielContainer container : spielMap.values()) {
            QuizGame quizGame = container.spiel();
            if (quizGame.getPlayerA().getId().equals(player.getId()) ||
                    quizGame.getPlayerB().getId().equals(player.getId())) {
                return quizGame;
            }
        }
        return null;
    }

    /**
     * Removes a player from the server. If the player is in a game, the game is ended and unregistered.
     * @param player the player leaving the server
     */
    public void removePlayer(Player player) {
        QuizGame currentGame = getGame(player);
        System.out.println("Player " + player.getUsername() + " left the server");
        if (currentGame != null) {
            currentGame.leaveGame(player);
        }
        connectedPlayers.remove(player);
    }

    /**
     * Returns a list of all active games.
     */
    public ArrayList<QuizGame> getGames() {
        return new ArrayList<>(spielMap.values().stream()
                .map(SpielContainer::spiel)
                .collect(Collectors.toList()));
    }

    /**
     * Main method to start the server
     * -p <port> to specify the port
     * --autostart to start the server automatically
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            quizReader = new QuizReader();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        instance = new Server();
        instance.startServer();
    }
}
