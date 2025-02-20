package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticsPanel extends JPanel {

    private MainGameFrame mainFrame;
    private JLabel finalScoreLabel;
    private JLabel winnerLabel;

    public StatisticsPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header im Quizduell-Stil
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Spiel beendet", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center-Bereich: Score und Gewinneranzeige
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        finalScoreLabel = new JLabel("Finaler Score: 0 - 0", SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        finalScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        winnerLabel = new JLabel("Gewinner: ", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(finalScoreLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(winnerLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom-Bereich: Button "Spiel beenden"
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton exitButton = new JButton("Spiel beenden");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setBackground(new Color(76, 175, 80));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset des Spiels und Rückkehr zur Lobby
                mainFrame.resetGame();
                mainFrame.showLobbyPanel();
            }
        });
        bottomPanel.add(exitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Setzt die finalen Statistiken, basierend auf den Spieler-Scores.
     */
    public void setFinalStatistics(int leftScore, int rightScore) {
        finalScoreLabel.setText("Finaler Score: " + leftScore + " - " + rightScore);
        String winner;
        if (leftScore > rightScore) {
            winner = "Gewinner: Spieler Links";
        } else if (rightScore > leftScore) {
            winner = "Gewinner: Spieler Rechts";
        } else {
            winner = "Unentschieden!";
        }
        winnerLabel.setText(winner);
    }
}
