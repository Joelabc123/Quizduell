package client.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LobbyHostPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JLabel hostLobbyIdLabel;
    private JLabel waitingLabel;

    public LobbyHostPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        // Vertikale Anordnung
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false); // Hintergrund wird in paintComponent gezeichnet
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // Füge vertikalen Glue hinzu, damit die Inhalte zentriert sind
        add(Box.createVerticalGlue());

        // Lobby-ID Label (3D-ähnlicher Effekt könnte z. B. durch Schatten erzielt werden)
        hostLobbyIdLabel = new JLabel("Lobby-ID: noch nicht verfügbar", SwingConstants.CENTER);
        hostLobbyIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        hostLobbyIdLabel.setForeground(Color.WHITE);
        hostLobbyIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(hostLobbyIdLabel);

        add(Box.createVerticalStrut(20));

        // Warte-Meldung
        waitingLabel = new JLabel("Warte auf Gegner...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        waitingLabel.setForeground(new Color(220, 220, 220)); // dezent helles Grau
        waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(waitingLabel);

        add(Box.createVerticalGlue());
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

    // Setter zum Aktualisieren der angezeigten Lobby-ID
    public void setLobbyId(String lobbyid) {
        hostLobbyIdLabel.setText("Lobby-ID: " + lobbyid);
    }
}
