package client;

import protocol.messages.ErrorMessage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientGUI gui;

    public final GameManager gameManager = new GameManager(this);

    public ClientHandler(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            gui = new ClientGUI();
            listenForMessages();

            //show error when connection to server fails

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Verbindung zum Server fehlgeschlagen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    Object received = in.readObject();

                    switch (received.getClass().getSimpleName()) {
                        case "LoginMessage":
                            protocol.messages.LoginMessage loginMessage = (protocol.messages.LoginMessage) received;

                            gameManager.loginMessage(loginMessage);
                            break;
                        case "LobbyStatusMessage":
                            protocol.messages.LobbyStatusMessage lobbyStatusMessage = (protocol.messages.LobbyStatusMessage) received;

                            gameManager.onLobbyStatusMessage(lobbyStatusMessage);
                            break;
                        case "ErrorMessage":
                            ErrorMessage errorMessage = (ErrorMessage) received;
                            if(errorMessage.getError().equals(ErrorType.SERVER_CLOSED)) {
                                showError("Server wurde geschlossen.");
                                System.exit(1);
                            }
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException  | ClassCastException e) {
                JOptionPane.showMessageDialog(null, "Verbindung unterbrochen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }).start();
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            showError("Nachricht konnte nicht gesendet werden.");
        }
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(gui, message, "Fehler", JOptionPane.ERROR_MESSAGE)
        );
    }
}