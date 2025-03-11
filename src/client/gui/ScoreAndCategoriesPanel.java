package client.gui;

import protocol.GameOutcome;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ScoreAndCategoriesPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JLabel leftPlayerNameLabel;
    private JLabel rightPlayerNameLabel;
    private JLabel scoreLabel;
    private JPanel categoriesPanel;
    private JButton chooseCategoryButton; // Als Klassenfeld

    private int leftScore = 0;
    private int rightScore = 0;

    public ScoreAndCategoriesPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        setOpaque(false); // Damit der benutzerdefinierte Hintergrund sichtbar bleibt
        initUI();
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public void resetScores() {
        leftScore = 0;
        rightScore = 0;
        scoreLabel.setText("Score: 0 - 0");
        categoriesPanel.removeAll();
        categoriesPanel.revalidate();
        categoriesPanel.repaint();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header: Bereich mit Spielernamen und Titel
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Spieler-Namen in dominanter Schrift
        leftPlayerNameLabel = new JLabel("Spieler Links", SwingConstants.LEFT);
        leftPlayerNameLabel.setForeground(Color.WHITE);
        leftPlayerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(leftPlayerNameLabel, BorderLayout.WEST);

        rightPlayerNameLabel = new JLabel("Spieler Rechts", SwingConstants.RIGHT);
        rightPlayerNameLabel.setForeground(Color.WHITE);
        rightPlayerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(rightPlayerNameLabel, BorderLayout.EAST);

        // Titel in großer, dominanter Schrift
        JLabel titleLabel = new JLabel("QUIZDUELL - Score & Kategorien", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(titleLabel, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // Center: Score-Anzeige und Kategorienliste
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Score-Label in sehr dominanter Schrift
        scoreLabel = new JLabel("Score: 0 - 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setForeground(Color.WHITE);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(scoreLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        categoriesPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(categoriesPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // Unten: Angepasster Button "Kategorie wählen"
        chooseCategoryButton = new JButton("Kategorie wählen");
        // Kleinere Breite, höhere Höhe
        chooseCategoryButton.setPreferredSize(new Dimension(150, 80));
        chooseCategoryButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        Color baseColor = new Color(255, 136, 73);
        chooseCategoryButton.setBackground(baseColor);
        chooseCategoryButton.setForeground(Color.WHITE);
        chooseCategoryButton.setFocusPainted(false);
        // 3D-Effekt: CompoundBorder mit BevelBorder und weißem LineBorder
        chooseCategoryButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        // Dynamischer Hover-Effekt
        chooseCategoryButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chooseCategoryButton.setBackground(baseColor.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chooseCategoryButton.setBackground(baseColor);
            }
        });
        chooseCategoryButton.addActionListener(e -> {
            System.out.println("Button 'Kategorie wählen' gedrückt. Externe Logik soll switchCategoryWheelPanel() aufrufen.");
            mainFrame.getClientHandler().gameManager.setCategories();
        });
        add(chooseCategoryButton, BorderLayout.SOUTH);
    }

    // Externe Setter für Spielernamen:
    public void setLeftPlayerName(String leftName) {
        leftPlayerNameLabel.setText(leftName);
    }

    public void setRightPlayerName(String rightName) {
        rightPlayerNameLabel.setText(rightName);
    }

    // Externe Methode zum Setzen beider Scores
    public void setScores(int left, int right) {
        this.leftScore = left;
        this.rightScore = right;
        updateScoreLabel();
    }

    // Externe Methode, um das Ergebnis einer Kategorie anzuzeigen.
    // Hier wird ein Hintergrundbalken in der Farbe #CAF0F8 (RGB 202,240,248) hinzugefügt.
    public void addCategoryWinner(String category, GameOutcome winner) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        // Setze den Hintergrundbalken
        rowPanel.setBackground(new Color(202, 240, 248));
        rowPanel.setOpaque(true);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel leftLabel = new JLabel("", SwingConstants.CENTER);
        JLabel catLabel = new JLabel(category, SwingConstants.CENTER);
        JLabel rightLabel = new JLabel("", SwingConstants.CENTER);

        // Lebendige, dominante Schriftzüge
        leftLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rightLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Farbgebung je nach Outcome
        if (winner == GameOutcome.DRAW) {
            leftLabel.setText("Unentschieden");
            rightLabel.setText("Unentschieden");
            leftLabel.setForeground(new Color(175, 129, 76));
            rightLabel.setForeground(new Color(175, 129, 76));
        } else if (winner == GameOutcome.PLAYER_A) {
            leftLabel.setText("Gewonnen");
            leftLabel.setForeground(new Color(76, 175, 80));
            rightLabel.setText("Verloren");
            rightLabel.setForeground(new Color(175, 76, 76));
        } else if (winner == GameOutcome.PLAYER_B) {
            rightLabel.setText("Gewonnen");
            rightLabel.setForeground(new Color(76, 175, 80));
            leftLabel.setText("Verloren");
            leftLabel.setForeground(new Color(175, 76, 76));
        } else {
            catLabel.setText(category + " - " + winner);
        }

        rowPanel.add(leftLabel, BorderLayout.WEST);
        rowPanel.add(catLabel, BorderLayout.CENTER);
        rowPanel.add(rightLabel, BorderLayout.EAST);

        categoriesPanel.add(rowPanel);
        categoriesPanel.add(Box.createVerticalStrut(5));

        updateScoreLabel();
        categoriesPanel.revalidate();
        categoriesPanel.repaint();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + leftScore + " - " + rightScore);
    }

    // Neue Methode zum Setzen bzw. Sichtbarmachen des "Kategorie wählen"-Buttons:
    public void setChooseCategoryButtonVisible(boolean visible) {
        chooseCategoryButton.setVisible(visible);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Hintergrund mit Farbverlauf: von hellblau oben zu dunkelblau unten
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 150, 199, 255), 0, height, new Color(144, 224, 239));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        super.paintComponent(g);
    }
}
