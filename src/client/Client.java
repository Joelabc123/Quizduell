package client;

import client.gui.MainGameFrame;

public class Client {
    private static int PORT = 12345;
    private static String HOST = "localhost";

    /**
     * Main method to start the client
     * --p PORT: set the port
     * --h HOST: set the host
     * @param args command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--p")) {
                PORT = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--h")) {
                HOST = args[i + 1];
            }
        }

        // Starte GUI
        MainGameFrame gui = new MainGameFrame();

        // Verbinde GUI mit dem ClientHandler
        ClientHandler clientHandler = new ClientHandler(HOST, PORT);

        // Speichere die ClientHandler-Instanz in der GUI für spätere Nutzung
        gui.setClientHandler(clientHandler);
    }
}
