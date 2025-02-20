package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreAndCategoriesPanel extends JPanel {

    private MainGameFrame mainFrame;

    // Labels für Spielernamen (links/rechts)
    private JLabel leftPlayerNameLabel;
    private JLabel rightPlayerNameLabel;

    private JLabel scoreLabel;
    private JPanel categoriesPanel;

    private int leftScore = 0;
    private int rightScore = 0;

    public ScoreAndCategoriesPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }
    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    /**
     * Setzt den Score und die Kategorienanzeige zurück.
     */
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

        // -- Header mit Quizduell-Blau und Spielernamen --
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Linke Seite: Spielername (links)
        leftPlayerNameLabel = new JLabel("Spieler Links", SwingConstants.LEFT);
        leftPlayerNameLabel.setForeground(Color.WHITE);
        leftPlayerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(leftPlayerNameLabel, BorderLayout.WEST);

        // Rechte Seite: Spielername (rechts)
        rightPlayerNameLabel = new JLabel("Spieler Rechts", SwingConstants.RIGHT);
        rightPlayerNameLabel.setForeground(Color.WHITE);
        rightPlayerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(rightPlayerNameLabel, BorderLayout.EAST);

        // Mitte: Überschrift
        JLabel titleLabel = new JLabel("QUIZDUELL - Score & Kategorien", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(titleLabel, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // -- Mittlerer Bereich: Score-Anzeige + Liste der Kategorien --
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Score Label
        scoreLabel = new JLabel("Score: 0 - 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(scoreLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Panel für Kategorien
        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        categoriesPanel.setBackground(Color.WHITE);

        // ScrollPane, falls später viele Kategorien angezeigt werden sollen
        JScrollPane scrollPane = new JScrollPane(categoriesPanel);
        scrollPane.setBorder(null);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // -- Unten: Button "Kategorie wählen" --
        JButton chooseCategoryButton = new JButton("Kategorie wählen");
        chooseCategoryButton.setBackground(new Color(76, 175, 80));
        chooseCategoryButton.setForeground(Color.WHITE);
        chooseCategoryButton.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCategoryButton.setFocusPainted(false);

        chooseCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wechsle zum Kategorienrad
                mainFrame.showCategoryWheelPanel();
            }
        });
        add(chooseCategoryButton, BorderLayout.SOUTH);
    }

    /**
     * Dummy-Methode zum Setzen der Spielernamen,
     * die später mit den Server-Daten befüllt werden kann.
     */
    public void setPlayerNames(String leftName, String rightName) {
        leftPlayerNameLabel.setText(leftName);
        rightPlayerNameLabel.setText(rightName);
    }

    /**
     * Fügt eine neue abgeschlossene Kategorie hinzu und aktualisiert den Score.
     */
    public void addCategoryResult(String category, String winner) {
        // Panel für die neue Zeile
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rowPanel.setBackground(new Color(230, 244, 255));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel leftLabel = new JLabel("", SwingConstants.CENTER);
        JLabel catLabel = new JLabel(category, SwingConstants.CENTER);
        JLabel rightLabel = new JLabel("", SwingConstants.CENTER);

        catLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rightLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        switch (winner) {
            case "LEFT":
                leftScore++;
                leftLabel.setText("Gewonnen");
                leftLabel.setForeground(new Color(76, 175, 80));
                break;
            case "RIGHT":
                rightScore++;
                rightLabel.setText("Gewonnen");
                rightLabel.setForeground(new Color(76, 175, 80));
                break;
            case "TIE":
                leftScore++;
                rightScore++;
                leftLabel.setText("Unentschieden");
                rightLabel.setText("Unentschieden");
                leftLabel.setForeground(Color.ORANGE);
                rightLabel.setForeground(Color.ORANGE);
                break;
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
}
