package server;

import protocol.messages.ErrorMessage;
import protocol.ErrorType;
import protocol.messages.*;
import utils.Usernames;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Player implements Runnable {

    private Server server;

    private Socket socket;
    private UUID id;
    private UUID secret;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    private String ip;

    public Player(Socket socket, Server server) {
        this.socket = socket;
        this.id = UUID.randomUUID();
        this.secret = UUID.randomUUID();
        this.ip = socket.getInetAddress().getHostAddress();
        this.username = Usernames.generate();
        this.server = server;
    }

    public String getUsername() { return username; }
    public String getIp() { return ip; }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            sendMessage(new LoginMessage(id, username));

            while (socket.isConnected()) {
                Object received = in.readObject();

                System.out.println("Received: " + received.getClass().getSimpleName());

                switch (received.getClass().getSimpleName()) {
                    case "HostLobbyMessage" -> {
                        HostLobbyMessage hostLobbyMessage = (HostLobbyMessage) received;
                        System.out.println("Player A hosted a game");

                        QuizGame game = new QuizGame(this, server);
                        game.addPlayer(this);



                        this.sendMessage(new LobbyStatusMessage(game.getId()));
                    }

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Verbindung mit " + username + " verloren.");
            server.removePlayer(this);
            server.removeFromQueue(this.getId());
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }
}