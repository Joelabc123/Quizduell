package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyJoinPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JTextField lobbyIdField;

    public LobbyJoinPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel instructionLabel = new JLabel("Geben Sie die Lobby-ID ein:", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        add(instructionLabel, BorderLayout.NORTH);

        lobbyIdField = new JTextField();
        lobbyIdField.setFont(new Font("Arial", Font.PLAIN, 22));
        lobbyIdField.setHorizontalAlignment(JTextField.CENTER);
        lobbyIdField.setPreferredSize(new Dimension(300, 50));
        add(lobbyIdField, BorderLayout.CENTER);

        JButton joinLobbyButton = new JButton("Lobby beitreten");
        joinLobbyButton.setFont(new Font("Arial", Font.BOLD, 24));
        joinLobbyButton.setBackground(new Color(76,175,80));
        joinLobbyButton.setForeground(Color.WHITE);
        joinLobbyButton.setFocusPainted(false);
        joinLobbyButton.setPreferredSize(new Dimension(300, 50));
        joinLobbyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lobbyId = lobbyIdField.getText().trim();
                if (lobbyId.isEmpty()) {
                    JOptionPane.showMessageDialog(LobbyJoinPanel.this, "Bitte geben Sie eine gültige Lobby-ID ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("Join-Lobby-Button gedrückt mit Lobby-ID: " + lobbyId + ". Externe Logik soll switchLobbyJoinPanel() aufrufen.");
                    mainFrame.getClientHandler().gameManager.joinLobby(Integer.parseInt(lobbyId));
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(joinLobbyButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showAuthenticationError() {
        JOptionPane.showMessageDialog(this, "Authentifizierung fehlgeschlagen. Bitte überprüfen Sie die Lobby-ID.", "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}
