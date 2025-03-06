package client.gui;

import javax.swing.*;
import java.awt.*;

public class LobbyHostPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JLabel hostLobbyIdLabel;

    public LobbyHostPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        hostLobbyIdLabel = new JLabel("Lobby-ID: ", SwingConstants.CENTER);
        hostLobbyIdLabel.setFont(new Font("Arial", Font.BOLD, 28));
        hostLobbyIdLabel.setForeground(new Color(30,144,255));
        add(hostLobbyIdLabel, BorderLayout.CENTER);
    }

    public void setLobbyId(String lobbyId) {
        hostLobbyIdLabel.setText("Lobby-ID: " + lobbyId);
    }
}
