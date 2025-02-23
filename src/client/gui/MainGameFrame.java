package client.gui;

import client.ClientHandler;

import javax.swing.*;
import java.awt.*;

public class MainGameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;  // Container für alle Screens

    private ClientHandler clientHandler;
    private ClientHandler client;
    // Screens
    private LobbyPanel lobbyPanel;
    private ScoreAndCategoriesPanel scorePanel;
    private CategoryWheelPanel categoryWheelPanel;
    private QuestionPanel questionPanel;
    private StatisticsPanel statisticsPanel;  // Statistik-Panel

    // Runden-Zähler (6 Durchläufe)
    private int roundsPlayed = 0;
    private final int MAX_ROUNDS = 6;

    public MainGameFrame() {
        super("Quizduell - Hauptfenster");
        initUI();
        initGameFlow();
    }


    public void connectToServer(String serverAddress, int port) {
        client = new ClientHandler(serverAddress, port);
    }
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panels erstellen
        lobbyPanel = new LobbyPanel(this);
        scorePanel = new ScoreAndCategoriesPanel(this);
        categoryWheelPanel = new CategoryWheelPanel(this);
        questionPanel = new QuestionPanel(this);
        statisticsPanel = new StatisticsPanel(this);

        // Panels dem CardLayout hinzufügen
        mainPanel.add(lobbyPanel, "lobby");
        mainPanel.add(scorePanel, "score");
        mainPanel.add(categoryWheelPanel, "wheel");
        mainPanel.add(questionPanel, "question");
        mainPanel.add(statisticsPanel, "statistics");

        add(mainPanel);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initGameFlow() {
        // Start in der Lobby
        showLobbyPanel();
    }

    public void showLobbyPanel() {
        cardLayout.show(mainPanel, "lobby");
    }

    public void showScorePanel() {
        cardLayout.show(mainPanel, "score");
    }

    public void showCategoryWheelPanel() {
        cardLayout.show(mainPanel, "wheel");
    }

    public void showQuestionPanel() {
        questionPanel.resetRound();
        cardLayout.show(mainPanel, "question");
    }

    public void showStatisticsPanel() {
        statisticsPanel.setFinalStatistics(scorePanel.getLeftScore(), scorePanel.getRightScore());
        cardLayout.show(mainPanel, "statistics");
    }

    /**
     * Wird vom LobbyPanel aufgerufen, nachdem die Lobby abgeschlossen wurde.
     */
    public void lobbyFinished() {
        // Dummy: Spielernamen setzen (später vom Server abfragen)
        scorePanel.setPlayerNames("Alice", "Bob");
        showScorePanel();
    }

    /**
     * Wird vom QuestionPanel aufgerufen, nachdem alle Fragen beantwortet wurden.
     */
    public void questionsCompleted(String chosenCategory, String winner) {
        scorePanel.addCategoryResult(chosenCategory, winner);
        roundsPlayed++;
        if (roundsPlayed < MAX_ROUNDS) {
            showScorePanel();
        } else {
            showStatisticsPanel();
        }
    }

    /**
     * Setzt den Spielzustand zurück und bringt die Lobby auf den Auswahlbildschirm ("start").
     */
    public void resetGame() {
        roundsPlayed = 0;
        scorePanel.resetScores();
        // Setze den LobbyPanel-Zustand zurück zum Startbildschirm
        lobbyPanel.resetToStart();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGameFrame frame = new MainGameFrame();
            frame.setVisible(true);
        });
    }
}
