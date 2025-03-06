package server;

import protocol.messages.ErrorMessage;
import protocol.messages.ErrorType;

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
    private ServerSocket serverSocket;
    private boolean running = false;
    private final List<Player> players = new ArrayList<>();
    private final List<Thread> clientThreads = new ArrayList<>();

    // HashMap, die sowohl das Spiel als auch den zugehörigen Thread speichert
    private final Map<UUID, GameContainer> gamesMap = new HashMap<>();

    private ServerGUI gui;


    // Innerer Container, der das QuizGame und den zugehörigen Thread zusammenhält
        private record GameContainer(QuizGame game, Thread thread) {
    }

    public Server() {
        this.gui = new ServerGUI(this);
        instance = this;
    }

    public void startServer() {
        if (running) return;

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                running = true;
                System.out.println("Server gestartet auf Port " + PORT);

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Neuer Client verbunden: " + clientSocket);

                    Player player = new Player(clientSocket, this);
                    players.add(player);

                    Thread clientThread = new Thread(player);
                    clientThreads.add(clientThread);
                    clientThread.start();

                    updatePlayerList();
                }
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }).start();
    }

    /**
     * Returns the players that are currently in the lobby (not in a game).
     */
    public ArrayList<Player> getPlayersInLobby() {
        ArrayList<Player> playersInLobby = new ArrayList<>();
        for (Player player : players) {
            if (!player.getIngame()) {
                playersInLobby.add(player);
            }
        }
        return playersInLobby;
    }

    /**
     * Stops the server, closes all connections and unregisters all games.
     */
    public void stopServer() {
        running = false;
        try {
            // Alle laufenden Spiele abmelden
            for (UUID gameId : new ArrayList<>(gamesMap.keySet())) {
                unregisterGame(gameId);
            }
            for (Player p : players) {
                p.sendMessage(new ErrorMessage(ErrorType.SERVER_CLOSED));
            }
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server gestoppt.");
            }
            for (Thread t : clientThreads) {
                t.interrupt();
            }
            players.clear();
            clientThreads.clear();
            updatePlayerList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new game. Der Server erstellt intern den Game-Thread und startet ihn.
     * @param game the game to register
     */
    public void registerGame(QuizGame game) {
        Thread gameThread = new Thread(game);
        GameContainer container = new GameContainer(game, gameThread);
        gamesMap.put(game.getGameState().getId(), container);
        gameThread.start();
    }

    /**
     * Unregisters a game with the given id by interrupting its thread and removing it from the map.
     * @param id id of the game to remove
     */
    public void unregisterGame(UUID id) {
        GameContainer container = gamesMap.get(id);
        if (container != null) {
            container.thread().interrupt();
            gamesMap.remove(id);
            System.out.println("Game " + id + " removed");
        }
    }

    /**
     * Returns the game the player is currently in.
     * @param player the player for whom to retrieve the game
     * @return the game the player is in or null if none
     */
    public synchronized QuizGame getGame(Player player) {
        for (GameContainer container : gamesMap.values()) {
            QuizGame game = container.game();
            if (game.getPlayerA().getId().equals(player.getId()) || game.getPlayerB().getId().equals(player.getId())) {
                return game;
            }
        }
        return null;
    }

    /**
     * Removes a player from the server. If the player is in a game, the game is ended and unregistered.
     * @param player the player leaving the server
     */
    public void removePlayer(Player player) {
        QuizGame game = getGame(player);
        System.out.println("Player " + player.getUsername() + " left the server");

        if (game != null) {
            game.leaveGame(player);
        }
        players.remove(player);
        updatePlayerList();
    }


    private void updatePlayerList() {
        StringBuilder sb = new StringBuilder();
        for (Player p : players) {
            sb.append(p.getUsername()).append(" (").append(p.getIp()).append(")\n");
        }
        gui.updatePlayerList(sb.toString());
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns a list of all active games.
     */
    public ArrayList<QuizGame> getGames() {
        return new ArrayList<>(gamesMap.values().stream().map(GameContainer::game).collect(Collectors.toList()));
    }

    /**
     * Main method to start the server
     * -p <port> to specify the port
     * --autostart to start the server automatically
     * @param args command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--p")) {
                PORT = Integer.parseInt(args[i + 1]);
            }
        }
        try {
            quizReader = new QuizReader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        instance = new Server();

        if (args.length >= 1 && args[0].contains("--autostart")) {
            instance.startServer();
        }
    }

}