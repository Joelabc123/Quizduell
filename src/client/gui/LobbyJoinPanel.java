package client.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

public class LobbyJoinPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JTextField lobbyIdField;
    private JButton joinLobbyButton;

    public LobbyJoinPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        // Verwenden eines BoxLayouts für vertikale Anordnung
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Damit der Hintergrund als Farbverlauf gezeichnet wird, überschreiben wir paintComponent
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Titel
        JLabel instructionLabel = new JLabel("Geben Sie die Lobby-ID ein:");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(instructionLabel);
        add(Box.createVerticalStrut(20));

        // Textfeld (kleiner)
        lobbyIdField = new JTextField();
        lobbyIdField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lobbyIdField.setHorizontalAlignment(JTextField.CENTER);
        lobbyIdField.setMaximumSize(new Dimension(200, 40));
        add(lobbyIdField);
        add(Box.createVerticalStrut(20));

        // Button-Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        joinLobbyButton = new JButton("Lobby beitreten");
        joinLobbyButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        joinLobbyButton.setBackground(new Color(76, 175, 80));
        joinLobbyButton.setForeground(Color.WHITE);
        joinLobbyButton.setFocusPainted(false);
        joinLobbyButton.setPreferredSize(new Dimension(220, 60));
        // 3D-Effekt:
        joinLobbyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // Action für den Button
        joinLobbyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinLobby();
            }
        });
        buttonPanel.add(joinLobbyButton);
        add(buttonPanel);

        // Ermögliche Enter-Key im Textfeld, den Button auszulösen:
        lobbyIdField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinLobbyButton.doClick();
            }
        });

        add(Box.createVerticalGlue());
    }

    private void joinLobby() {
        String lobbyId = lobbyIdField.getText().trim();
        if (lobbyId.isEmpty()) {
            JOptionPane.showMessageDialog(LobbyJoinPanel.this, "Bitte geben Sie eine gültige Lobby-ID ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("Join-Lobby-Button gedrückt mit Lobby-ID: " + lobbyId + ". Externe Logik soll switchLobbyJoinPanel() aufrufen.");
            try {
                int id = Integer.parseInt(lobbyId);
                mainFrame.getClientHandler().gameManager.joinLobby(id);
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(LobbyJoinPanel.this, "Ungültige Lobby-ID.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    public void showAuthenticationError() {
        JOptionPane.showMessageDialog(this, "Authentifizierung fehlgeschlagen. Bitte überprüfen Sie die Lobby-ID.", "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}
