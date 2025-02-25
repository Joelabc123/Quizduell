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

    private static QuizReader quizReader;

    private static Server instance;

    private static int PORT = 12345;
    private ServerSocket serverSocket;
    private boolean running = false;
    private final List<Player> players = new ArrayList<>();
    private final List<Thread> clientThreads = new ArrayList<>();

    private ArrayList<QuizGame> games = new ArrayList<>();

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
                p.sendMessage(new ErrorMessage("Server closed", ErrorType.SERVER_CLOSED));
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