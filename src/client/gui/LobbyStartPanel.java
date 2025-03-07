package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyStartPanel extends JPanel {
    private MainGameFrame mainFrame;

    public LobbyStartPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(30,144,255));

        JLabel titleLabel = new JLabel("Willkommen bei QUIZDUELL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.setOpaque(false);

        JButton joinButton = new JButton("Spiel beitreten");
        JButton hostButton = new JButton("Spiel hosten");

        joinButton.setFont(new Font("Arial", Font.BOLD, 20));
        hostButton.setFont(new Font("Arial", Font.BOLD, 20));

        joinButton.setBackground(new Color(76,175,80));
        hostButton.setBackground(new Color(244,67,54));
        joinButton.setForeground(Color.WHITE);
        hostButton.setForeground(Color.WHITE);
        joinButton.setFocusPainted(false);
        hostButton.setFocusPainted(false);

        joinButton.setPreferredSize(new Dimension(200, 50));
        hostButton.setPreferredSize(new Dimension(200, 50));

        // Statt direktem Scene-Switching: Nur Nachricht ausgeben
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

        buttonPanel.add(joinButton);
        buttonPanel.add(hostButton);
        add(buttonPanel, BorderLayout.CENTER);
    }
}
