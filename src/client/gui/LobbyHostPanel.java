package client.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
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
        this.mainFrame.setLobbyHostPanel(this); // Registrierung des Panels im MainGameFrame
        initUI();
        loadQuizSets();
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

        // Sobald ein neues Element ausgewählt wird, wird geprüft, ob das Quizset gültig ist.
        // Sind mindestens 8 Kategorien vorhanden und besitzt jede Kategorie mindestens 3 Fragen,
        // wird die Methode sendQuizset (über den GameManager) aufgerufen.
        quizSetComboBox.addActionListener(e -> {
            String selectedQuiz = getSelectedQuizSet();
            if (selectedQuiz != null && !selectedQuiz.isEmpty()) {
                if (!validateQuizset(selectedQuiz)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ungültiges Quizset.\nEs müssen mindestens 8 Kategorien vorhanden sein und jede Kategorie muss mindestens 3 Fragen haben.",
                            "Quizset Fehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    mainFrame.getClientHandler().gameManager.sendQuizset(selectedQuiz);
                }
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

    // Gibt das aktuell ausgewählte Quizset zurück.
    public String getSelectedQuizSet() {
        Object selected = quizSetComboBox.getSelectedItem();
        return selected != null ? selected.toString() : null;
    }

    /**
     * Prüft, ob das ausgewählte Quizset folgende Bedingungen erfüllt:
     * - Es sind mindestens 8 Kategorien vorhanden.
     * - Jede Kategorie besitzt mindestens 3 Fragen.
     *
     * Dazu wird das XML-Dokument des Quizsets geparst.
     *
     * @param fileName der Name der XML-Datei (im Ordner "resources")
     * @return true, wenn beide Bedingungen erfüllt sind, sonst false.
     */
    private boolean validateQuizset(String fileName) {
        try {
            File file = new File("resources", fileName);
            if (!file.exists() || !file.isFile()) {
                return false;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Anzahl der Kategorien prüfen
            NodeList categoryList = doc.getElementsByTagName("Kategorie");
            if (categoryList.getLength() < 8) {
                return false;
            }

            // Anzahl der Fragen pro Kategorie prüfen:
            NodeList questionList = doc.getElementsByTagName("Frage");

            // Für jede Kategorie ermitteln, wie viele Fragen ihr zugeordnet sind
            for (int i = 0; i < categoryList.getLength(); i++) {
                Element categoryElem = (Element) categoryList.item(i);
                String katID = categoryElem.getAttribute("KatID");
                int count = 0;
                for (int j = 0; j < questionList.getLength(); j++) {
                    Element questionElem = (Element) questionList.item(j);
                    String questionKatID = questionElem.getAttribute("KID");
                    if (katID.equals(questionKatID)) {
                        count++;
                    }
                }
                if (count < 3) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
