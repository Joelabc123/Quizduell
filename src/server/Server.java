package server;

import protocol.messages.ErrorMessage;
import protocol.messages.ErrorType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {

    public static QuizReader quizReader = new QuizReader();

    private static Server instance;

    private static int PORT = 12345;
    private ServerSocket serverSocket;
    private boolean running = false;
    private final List<Player> players = new ArrayList<>();
    private final List<Thread> clientThreads = new ArrayList<>();

    private ArrayList<QuizGame> games = new ArrayList<>();
    private ArrayList<Thread> gameThreads = new ArrayList<>();

    private ServerGUI gui;

    public Server() {
        this.gui = new ServerGUI(this);
        instance = this;
    }

    public synchronized void startServer() {
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
                }
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }).start();
    }

    public synchronized void stopServer() {
        running = false;
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new game and starts the game thread
     * @param game game to register
     * @param thread thread to start
     */
    public synchronized void registerGame(QuizGame game, Thread thread) {
        games.add(game);
        gameThreads.add(thread);

        thread.start();
    }

    /**
     * Removes a game from the list of active games
     * @param id id of the game to remove
     */
    public synchronized void unregisterGame(UUID id) {
        QuizGame targetGame = null;
        Thread targetThread = null;
        for (QuizGame game : games) {
            if (game.getGameState().getId().equals(id)) {
                targetGame = game;
                targetThread = gameThreads.get(games.indexOf(game));
            }
        }

        if (targetGame != null) {
            //Stop the game thread
            targetThread.interrupt();
            games.remove(targetGame);
        }
    }

    public ArrayList<QuizGame> getGames() {
        return games;
    }

    public QuizGame getGameFromPlayer(Player player) {
        for (QuizGame game : games) {
            if (game.getPlayerA() != null && game.getPlayerA().equals(player)) {
                return game;
            }
            if (game.getPlayerB() != null && game.getPlayerB().equals(player)) {
                return game;
            }
        }
        return null;
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