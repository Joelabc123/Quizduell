package client.gui;

import javax.swing.*;
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
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Panel für Info (Lobby-ID und Warte-Meldung)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        hostLobbyIdLabel = new JLabel("Lobby-ID: " + "noch nicht verfügbar", SwingConstants.CENTER);
        hostLobbyIdLabel.setFont(new Font("Arial", Font.BOLD, 28));
        hostLobbyIdLabel.setForeground(new Color(30, 144, 255));
        hostLobbyIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(hostLobbyIdLabel);

        infoPanel.add(Box.createVerticalStrut(20));

        waitingLabel = new JLabel("Warte auf Gegner...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        waitingLabel.setForeground(new Color(100, 100, 100));
        waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(waitingLabel);

        add(infoPanel, BorderLayout.CENTER);
    }

    public void setLobbyId(String lobbyid) {
        hostLobbyIdLabel.setText("Lobby-ID: " + lobbyid);
    }
}
