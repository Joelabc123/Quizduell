package client.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
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
        setOpaque(false); // Damit der Gradient-Hintergrund sichtbar wird

        // Banner-Panel oben: Farbe #0077B6 für bessere Sichtbarkeit des Titels
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setOpaque(true);
        bannerPanel.setBackground(Color.decode("#0077B6"));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Spiel beendet", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        bannerPanel.add(titleLabel, BorderLayout.CENTER);
        add(bannerPanel, BorderLayout.NORTH);

        // Center-Panel für Statistiken
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
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

        // Bottom-Panel für den Button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton exitButton = getJButton();
        bottomPanel.add(exitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton getJButton() {
        JButton exitButton = new JButton("Spiel beenden");
        exitButton.setFont(new Font("Arial", Font.BOLD, 22));
        exitButton.setBackground(new Color(76, 175, 80));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setPreferredSize(new Dimension(200, 50));
        // 3D-Effekt durch BevelBorder
        exitButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.resetGame();
                mainFrame.switchLobbyStartPanel();
                System.out.println("Exit-Button gedrückt. Externe Logik muss resetGame() und switchLobbyStartPanel() aufrufen.");
            }
        });
        return exitButton;
    }

    public void setFinalStatistics(int leftScore, int rightScore, String usernameLeft, String usernameRight) {
        finalScoreLabel.setText("Finaler Score: " + leftScore + " - " + rightScore);
        String winner;
        if (leftScore > rightScore) {
            winner = "Gewinner: " + usernameLeft;
        } else if (rightScore > leftScore) {
            winner = "Gewinner: " + usernameRight;
        } else {
            winner = "Unentschieden!";
        }
        winnerLabel.setText(winner);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Hintergrund mit Farbverlauf: von dunkelblau oben zu hellblau unten
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 150, 199, 255), 0, height, new Color(144, 224, 239));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
    }
}
