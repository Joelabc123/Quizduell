package client.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LobbyHostPanel extends JPanel {
    private MainGameFrame mainFrame;
    private JLabel hostLobbyIdLabel;
    private JLabel waitingLabel;
    private JComboBox<String> quizSetComboBox;
    // startQuizButton wird hier nicht genutzt – falls benötigt, einfach einfügen

    public LobbyHostPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        loadQuizSets();
        // Hier wird die Lobby-ID aktualisiert.
        // Ersetze generateLobbyId() ggf. durch deinen Lobby-ID-Provider (z.B. mainFrame.getLobbyId())
        setLobbyId(generateLobbyId());
    }

    private void initUI() {
        // Vertikale Anordnung
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false); // Hintergrund wird in paintComponent gezeichnet
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // Vertikale Glue für Zentrierung
        add(Box.createVerticalGlue());

        // Lobby-ID Label
        hostLobbyIdLabel = new JLabel("Lobby-ID: noch nicht verfügbar", SwingConstants.CENTER);
        hostLobbyIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        hostLobbyIdLabel.setForeground(Color.WHITE);
        hostLobbyIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(hostLobbyIdLabel);

        add(Box.createVerticalStrut(20));

        // Warte-Meldung
        waitingLabel = new JLabel("Warte auf Gegner...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        waitingLabel.setForeground(new Color(220, 220, 220));
        waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(waitingLabel);

        add(Box.createVerticalStrut(20));

        // Label zur Auswahl des Quizsets
        JLabel quizSetLabel = new JLabel("Wähle ein Quizset aus:", SwingConstants.CENTER);
        quizSetLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        quizSetLabel.setForeground(Color.WHITE);
        quizSetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(quizSetLabel);

        add(Box.createVerticalStrut(10));

        // ComboBox zur Anzeige der Quizsets
        quizSetComboBox = new JComboBox<>();
        quizSetComboBox.setMaximumSize(new Dimension(300, 30));
        quizSetComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(quizSetComboBox);

        // Sobald ein neues Element ausgewählt wird, wird sendQuizset aufgerufen
        quizSetComboBox.addActionListener(e -> {
            String selectedQuiz = getSelectedQuizSet();
            if (selectedQuiz != null && !selectedQuiz.isEmpty()) {
                // Hier wird die Methode sendQuizset aufgerufen.
                // Annahme: MainGameFrame hat die Methode sendQuizset(String quizset)
                mainFrame.getClientHandler().gameManager.sendQuizset(selectedQuiz);
            }
        });

        add(Box.createVerticalStrut(10));

        add(Box.createVerticalGlue());
    }

    // Liest den Ordner "resources" aus und fügt alle XML-Dateien der ComboBox hinzu
    private void loadQuizSets() {
        File resourcesFolder = new File("resources");
        if (resourcesFolder.exists() && resourcesFolder.isDirectory()) {
            File[] xmlFiles = resourcesFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            if (xmlFiles != null && xmlFiles.length > 0) {
                List<String> fileNames = new ArrayList<>();
                for (File file : xmlFiles) {
                    fileNames.add(file.getName());
                }
                for (String fileName : fileNames) {
                    quizSetComboBox.addItem(fileName);
                }
            } else {
                quizSetComboBox.addItem("Keine Quizsets gefunden");
            }
        } else {
            quizSetComboBox.addItem("Ressourcenordner nicht gefunden");
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

    // Setter zum Aktualisieren der angezeigten Lobby-ID
    public void setLobbyId(String lobbyid) {
        hostLobbyIdLabel.setText("Lobby-ID: " + lobbyid);
    }

    // Beispielmethode zur Erzeugung einer zufälligen Lobby-ID.
    // Ersetze diesen Code gegebenenfalls durch die tatsächliche Logik (z.B. mainFrame.getLobbyId()).
    private String generateLobbyId() {
        int randomId = (int) (Math.random() * 9000) + 1000; // erzeugt eine 4-stellige Zahl
        return String.valueOf(randomId);
    }

    // Gibt das aktuell ausgewählte Quizset zurück.
    public String getSelectedQuizSet() {
        Object selected = quizSetComboBox.getSelectedItem();
        return selected != null ? selected.toString() : null;
    }
}
