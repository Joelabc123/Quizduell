package client.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LobbyStartPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JLabel welcomeLabel; // Label für die Willkommensnachricht

    public LobbyStartPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        setOpaque(false); // Verhindert, dass der Standardhintergrund gezeichnet wird
        initUI();
    }

    private void initUI() {
        // Verwenden Sie BoxLayout für eine vertikale Anordnung
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(Box.createVerticalStrut(20));

        // Willkommensnachricht
        welcomeLabel = new JLabel("Guten Tag, Spieler");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(new EmptyBorder(0, 20, 10, 20));
        add(welcomeLabel);

        // Titel
        JLabel titleLabel = new JLabel("Willkommen bei QUIZDUELL");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 20, 10, 20));
        add(titleLabel);

        // Untertitel / Beschreibung
        JLabel subtitleLabel = new JLabel("Wählen Sie, ob Sie ein Spiel hosten oder beitreten möchten.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(0, 20, 40, 20));
        add(subtitleLabel);

        // Button-Panel mit FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setOpaque(false);

        JButton joinButton = new JButton("Spiel beitreten");
        JButton hostButton = new JButton("Spiel hosten");
        JButton createQuizButton = new JButton("Eigenes Quiz erstellen");

        // Schrift und Größe für alle Buttons
        joinButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        hostButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        createQuizButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        joinButton.setPreferredSize(new Dimension(220, 60));
        hostButton.setPreferredSize(new Dimension(220, 60));
        createQuizButton.setPreferredSize(new Dimension(220, 60));

        // Standardfarben
        Color joinColor = new Color(76, 175, 80);   // Grün
        Color hostColor = new Color(244, 67, 54);     // Rot
        Color createQuizColor = new Color(33, 150, 243); // Blau

        joinButton.setBackground(joinColor);
        hostButton.setBackground(hostColor);
        createQuizButton.setBackground(createQuizColor);
        joinButton.setForeground(Color.WHITE);
        hostButton.setForeground(Color.WHITE);
        createQuizButton.setForeground(Color.WHITE);

        // 3D-Effekt durch BevelBorder
        joinButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        hostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        createQuizButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // Fokusanzeige deaktivieren
        joinButton.setFocusPainted(false);
        hostButton.setFocusPainted(false);
        createQuizButton.setFocusPainted(false);

        // Dynamischer Mouse-Hover-Effekt für joinButton
        joinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                joinButton.setBackground(joinColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                joinButton.setBackground(joinColor);
            }
        });
        // Dynamischer Mouse-Hover-Effekt für hostButton
        hostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hostButton.setBackground(hostColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hostButton.setBackground(hostColor);
            }
        });
        // Dynamischer Mouse-Hover-Effekt für createQuizButton
        createQuizButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                createQuizButton.setBackground(createQuizColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                createQuizButton.setBackground(createQuizColor);
            }
        });

        // ActionListener: Beim Klick wird die entsprechende Logik aufgerufen
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchLobbyJoinPanel();
                System.out.println("Join-Button gedrückt. Externe Logik soll switchLobbyJoinPanel() aufrufen.");
            }
        });
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getClientHandler().gameManager.hostLobby();
                mainFrame.switchLobbyHostPanel();
                System.out.println("Host-Button gedrückt. Externe Logik soll switchLobbyHostPanel() aufrufen.");
            }
        });
        createQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hier können Sie die Logik für das Erstellen eines eigenen Quiz einbinden
                System.out.println("Eigenes Quiz erstellen-Button gedrückt. Externe Logik soll eigene Quiz-Erstellung aufrufen.");
                mainFrame.switchCreateQuizPane();
            }
        });

        buttonPanel.add(joinButton);
        buttonPanel.add(hostButton);
        buttonPanel.add(createQuizButton);
        add(buttonPanel);

        add(Box.createVerticalGlue());
    }

    // Setter zum Setzen des Spielernamens und Aktualisieren der Willkommensnachricht
    public void setPlayerName(String name) {
        welcomeLabel.setText("Guten Tag, " + name);
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
