package client.gui;

import client.ClientHandler;
import javax.swing.*;
import java.awt.*;

public class MainGameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;  // Container für alle Szenen

    // Szenen (Panels) – werden on‑demand erstellt:
    private LobbyStartPanel lobbyStartPanel;
    private LobbyJoinPanel lobbyJoinPanel;
    private LobbyHostPanel lobbyHostPanel;
    private ScoreAndCategoriesPanel scorePanel;
    private CategoryWheelPanel categoryWheelPanel;
    private QuestionPanel questionPanel;
    private StatisticsPanel statisticsPanel;
    private CreateQuizPanel createQuizPanel; // Neues Panel für die Quiz-Erstellung

    // Runden-Zähler (z. B. 6 Runden)
    private int roundsPlayed = 0;
    private final int MAX_ROUNDS = 6;

    // ClientHandler (wird vom Client gesetzt)
    private ClientHandler clientHandler;

    public MainGameFrame(ClientHandler clientHandler) {
        super("Quizduell - Hauptfenster");
        this.clientHandler = clientHandler;
        createMainPanel();
        createLobbyStartScene();  // Nur die LobbyStart-Szene wird initial erzeugt
        // Direkt im Konstruktor: Fenstergröße, Position und Close-Operation festlegen
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        switchLobbyStartPanel();
        setVisible(true);
    }

    // Erzeugt den Hauptcontainer (mainPanel) mit CardLayout und fügt ihn dem JFrame hinzu
    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    // Erzeugt und fügt das LobbyStartPanel zum mainPanel hinzu
    private void createLobbyStartScene() {
        lobbyStartPanel = new LobbyStartPanel(this);
        mainPanel.add(lobbyStartPanel, "lobbyStart");
    }

    // Szenen werden on‑demand erstellt:
    public void switchLobbyJoinPanel() {
        if (lobbyJoinPanel == null) {
            lobbyJoinPanel = new LobbyJoinPanel(this);
            mainPanel.add(lobbyJoinPanel, "lobbyJoin");
        }
        cardLayout.show(mainPanel, "lobbyJoin");
    }

    public void switchLobbyHostPanel() {
        if (lobbyHostPanel == null) {
            lobbyHostPanel = new LobbyHostPanel(this);
            mainPanel.add(lobbyHostPanel, "lobbyHost");
        }
        cardLayout.show(mainPanel, "lobbyHost");
    }

    public void switchScorePanel() {
        if (scorePanel == null) {
            scorePanel = new ScoreAndCategoriesPanel(this);
            mainPanel.add(scorePanel, "score");
        }
        cardLayout.show(mainPanel, "score");
    }

    public void switchCategoryWheelPanel() {
        if (categoryWheelPanel == null) {
            categoryWheelPanel = new CategoryWheelPanel(this);
            mainPanel.add(categoryWheelPanel, "wheel");
        }
        cardLayout.show(mainPanel, "wheel");
    }

    public void switchQuestionPanel() {
        if (questionPanel == null) {
            questionPanel = new QuestionPanel(this);
            mainPanel.add(questionPanel, "question");
        }
        questionPanel.resetRound();
        cardLayout.show(mainPanel, "question");
    }

    public void switchStatisticsPanel() {
        if (statisticsPanel == null) {
            statisticsPanel = new StatisticsPanel(this);
            mainPanel.add(statisticsPanel, "statistics");
        }
        cardLayout.show(mainPanel, "statistics");
    }

    public void switchLobbyStartPanel() {
        cardLayout.show(mainPanel, "lobbyStart");
    }

    /**
     * Wechselt zur Szene der Quiz-Erstellung.
     * Falls das Panel noch nicht existiert, wird es erstellt und dem mainPanel hinzugefügt.
     */
    public void switchCreateQuizPane() {
        if (createQuizPanel == null) {
            createQuizPanel = new CreateQuizPanel(this);
            mainPanel.add(createQuizPanel, "createQuiz");
        }
        cardLayout.show(mainPanel, "createQuiz");
    }

    /**
     * Setzt den Spielzustand zurück und wechselt zur LobbyStart-Szene.
     */
    public void resetGame() {
        roundsPlayed = 0;
        if (scorePanel != null) {
            scorePanel.resetScores();
        }
        switchLobbyStartPanel();
    }

    // Getter und Setter

    public LobbyStartPanel getLobbyStartPanel() {
        return lobbyStartPanel;
    }

    public LobbyJoinPanel getLobbyJoinPanel() {
        return lobbyJoinPanel;
    }

    public LobbyHostPanel getLobbyHostPanel() {
        return lobbyHostPanel;
    }

    public ScoreAndCategoriesPanel getScorePanel() {
        return scorePanel;
    }

    public CategoryWheelPanel getCategoryWheelPanel() {
        return categoryWheelPanel;
    }

    public QuestionPanel getQuestionPanel() {
        return questionPanel;
    }

    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setLobbyHostPanel(LobbyHostPanel lobbyHostPanel) {
        this.lobbyHostPanel = lobbyHostPanel;
    }
}
