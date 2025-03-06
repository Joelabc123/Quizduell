package client;

import client.gui.MainGameFrame;
import protocol.messages.ErrorMessage;
import protocol.messages.ErrorType;
import protocol.messages.LoginMessage;
import protocol.messages.UpdateGameMessage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private MainGameFrame gui;

    public GameManager gameManager = new GameManager(this);

    public ClientHandler(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            gui = new MainGameFrame(this);
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

                    System.out.println("Received: " + received.getClass().getSimpleName());

                    switch (received.getClass().getSimpleName()) {
                        case "LoginMessage":
                            LoginMessage loginMessage = (LoginMessage) received;

                            gameManager.loginMessage(loginMessage);
                            break;
                        case "UpdateGameMessage":
                            UpdateGameMessage updateGameMessage = (UpdateGameMessage) received;

                            gameManager.updateGameMessage(updateGameMessage);
                            break;
                        case "ErrorMessage":
                            try {
                                ErrorMessage errorMessage = (ErrorMessage) received;
                                if(errorMessage.getErrorType().equals(ErrorType.SERVER_CLOSED)) {
                                    showError("Server wurde geschlossen.");
                                    System.exit(1);
                                }
                                if(errorMessage.getErrorType().equals(ErrorType.INVALID_ACTION)) {
                                    showError("Invalide Aktion.");
                                    System.exit(1);
                                }
                                if(errorMessage.getErrorType().equals(ErrorType.UNKNOWN_ERROR)) {
                                    showError("Unbekannter Fehler.");
                                    System.exit(1);
                                }
                            } catch (ClassCastException e) {
                                showError("Fehler beim Empfangen der Nachricht.");
                            }
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException  | ClassCastException e) {
                e.printStackTrace();
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