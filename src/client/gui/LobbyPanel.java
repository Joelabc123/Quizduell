package client.gui;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel extends JPanel {

    private CardLayout lobbyCardLayout;
    private MainGameFrame mainFrame;

    // Komponenten für das Join-Panel
    private JTextField lobbyIdField;
    // Komponenten für das Host-Panel
    private JLabel hostLobbyIdLabel;

    public LobbyPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        lobbyCardLayout = new CardLayout();
        setLayout(lobbyCardLayout);

        // Erstelle und füge die drei Sub-Panels hinzu:
        add(createStartPanel(), "start");
        add(createJoinPanel(), "join");
        add(createHostPanel(), "host");

        // Zeige zunächst das Start-Panel an
        lobbyCardLayout.show(this, "start");
    }

    /**
     * Zeigt den Start-Bildschirm (Auswahl: beitreten oder hosten) an.
     */
    public void resetToStart() {
        lobbyCardLayout.show(this, "start");
    }

    /**
     * Start-Panel: Auswahl, ob man einem Spiel beitreten oder ein Spiel hosten möchte.
     */
    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 144, 255)); // Quizduell-Blau

        // Großes Begrüßungstext
        JLabel titleLabel = new JLabel("Willkommen bei QUIZDUELL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Button-Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setOpaque(false);

        JButton joinButton = new JButton("Spiel beitreten");
        JButton hostButton = new JButton("Spiel hosten");

        joinButton.setFont(new Font("Arial", Font.BOLD, 20));
        hostButton.setFont(new Font("Arial", Font.BOLD, 20));

        // Setze farbige Buttons: Grün für beitreten, Rot für hosten
        joinButton.setBackground(new Color(76, 175, 80));
        hostButton.setBackground(new Color(244, 67, 54));
        joinButton.setForeground(Color.WHITE);
        hostButton.setForeground(Color.WHITE);
        joinButton.setFocusPainted(false);
        hostButton.setFocusPainted(false);

        joinButton.setPreferredSize(new Dimension(200, 50));
        hostButton.setPreferredSize(new Dimension(200, 50));

        joinButton.addActionListener(e -> lobbyCardLayout.show(this, "join"));
        hostButton.addActionListener(e -> {
            hostLobbyIdLabel.setText("Lobby-ID: " + generateLobbyId());
            lobbyCardLayout.show(this, "host");
        });
        buttonPanel.add(joinButton);
        buttonPanel.add(hostButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Join-Panel: Hier kann der Spieler die Lobby-ID eingeben.
     */
    private JPanel createJoinPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel instructionLabel = new JLabel("Geben Sie die Lobby-ID ein:", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(instructionLabel, BorderLayout.NORTH);

        lobbyIdField = new JTextField();
        lobbyIdField.setFont(new Font("Arial", Font.PLAIN, 22));
        lobbyIdField.setHorizontalAlignment(JTextField.CENTER);
        lobbyIdField.setPreferredSize(new Dimension(300, 50));
        panel.add(lobbyIdField, BorderLayout.CENTER);

        JButton joinLobbyButton = new JButton("Lobby beitreten");
        joinLobbyButton.setFont(new Font("Arial", Font.BOLD, 24));
        joinLobbyButton.setBackground(new Color(76, 175, 80));
        joinLobbyButton.setForeground(Color.WHITE);
        joinLobbyButton.setFocusPainted(false);
        joinLobbyButton.setPreferredSize(new Dimension(300, 50));
        joinLobbyButton.addActionListener(e -> {
            String lobbyId = lobbyIdField.getText().trim();
            if (lobbyId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Lobby-ID ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            } else {
                dummyJoinLobby(lobbyId);
                mainFrame.lobbyFinished();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(joinLobbyButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Host-Panel: Zeigt eine generierte Lobby-ID und einen Button zum Starten des Spiels.
     */
    private JPanel createHostPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        hostLobbyIdLabel = new JLabel("Lobby-ID: ", SwingConstants.CENTER);
        hostLobbyIdLabel.setFont(new Font("Arial", Font.BOLD, 28));
        hostLobbyIdLabel.setForeground(new Color(30, 144, 255));
        panel.add(hostLobbyIdLabel, BorderLayout.NORTH);

        JButton startGameButton = new JButton("Spiel starten");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 24));
        startGameButton.setBackground(new Color(76, 175, 80));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setFocusPainted(false);
        startGameButton.setPreferredSize(new Dimension(300, 50));
        startGameButton.addActionListener(e -> {
            dummyHostLobby();
            mainFrame.lobbyFinished();
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(startGameButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Dummy-Methode zur Generierung einer Lobby-ID.
    private String generateLobbyId() {
        return "LOBBY1234";
    }

    // Dummy-Methode zum Beitreten in eine Lobby.
    private void dummyJoinLobby(String lobbyId) {
        System.out.println("Joining lobby: " + lobbyId);
    }

    // Dummy-Methode zum Hosten einer Lobby.
    private void dummyHostLobby() {
        System.out.println("Hosting lobby with id: " + hostLobbyIdLabel.getText());
    }
}
