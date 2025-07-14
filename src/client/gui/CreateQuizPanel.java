package client.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CreateQuizPanel extends JPanel {

    private MainGameFrame mainFrame;

    private JTabbedPane tabbedPane;
    //Komponenten für Fragen erstellen
    private JPanel questionPanel;
    private JTextArea questionTextArea;
    private JTextField answerField1, answerField2, answerField3, answerField4;
    private JRadioButton radio1, radio2, radio3, radio4;
    private ButtonGroup correctAnswerGroup;
    private JButton addQuestionButton, finishQuestionButton;
    private JComboBox<String> categoryComboBox; // Auswahl, in welche Kategorie die Frage gehört
    private int nextFID = 1;

    //Komponenten für Fragen löschen
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;
    private JButton deleteQuestionButton;

    //Komponenten für Kategorie erstellen
    private JPanel categoryPanel;
    private JTextField categoryNameField;
    private JButton addCategoryButton;

    //Komponenten für Kategorie löschen
    private DefaultListModel<String> categoryDeletionListModel;
    private JList<String> categoryDeletionList;
    private JButton deleteCategoryButton;

    //Panel mit Dateiliste, Buttons und Kategorienanzeige aus der ausgewählten XML-Datei
    private JPanel rightPanel;
    private JList<String> xmlFileList;
    private DefaultListModel<String> xmlFileListModel;
    private JList<String> fileCategoryList;
    private DefaultListModel<String> fileCategoryListModel;
    private JButton saveSelectedXmlButton;
    private JButton createNewXmlButton;
    private JButton deleteXmlButton;

    private final String resourcesDir = "resources/";

    private Document quizDocument;
    private Element rootDatabase; // Root: <database>
    private Element kategorienElement; // <Kategorien>
    private Element fragenElement;     // <Fragen>
    private Element loesungenElement;   // <Lösungen>

    // Für das aktuell im rechten Bereich gewählte XML-Dokument
    private Document selectedXmlDocument;
    private File selectedXmlFile;

    private Map<String, String> categoryMap = new HashMap<>();

    private int nextKatID = 1;

    public CreateQuizPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initNewQuizDocument();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Fragen erstellen", createQuestionPanel());
        tabbedPane.addTab("Kategorie erstellen", createCategoryPanel());
        add(tabbedPane, BorderLayout.CENTER);

        rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);
    }

    private void initNewQuizDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            quizDocument = builder.newDocument();
            rootDatabase = quizDocument.createElement("database");
            quizDocument.appendChild(rootDatabase);
            kategorienElement = quizDocument.createElement("Kategorien");
            rootDatabase.appendChild(kategorienElement);
            fragenElement = quizDocument.createElement("Fragen");
            rootDatabase.appendChild(fragenElement);
            loesungenElement = quizDocument.createElement("Lösungen");
            rootDatabase.appendChild(loesungenElement);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Erstellen des neuen XML-Dokuments: " + ex.getMessage(),
                    "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createQuestionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel questionLabel = new JLabel("Frage:");
        questionTextArea = new JTextArea(3, 40);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(questionTextArea);
        inputPanel.add(questionLabel);
        inputPanel.add(questionScrollPane);

        JPanel answersPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        answersPanel.setOpaque(false);
        correctAnswerGroup = new ButtonGroup();

        // Antwort 1
        JPanel answerPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerPanel1.setOpaque(false);
        radio1 = new JRadioButton();
        radio1.setOpaque(false);
        correctAnswerGroup.add(radio1);
        answerField1 = new JTextField(30);
        answerPanel1.add(radio1);
        answerPanel1.add(new JLabel("Antwort 1:"));
        answerPanel1.add(answerField1);
        answersPanel.add(answerPanel1);

        // Antwort 2
        JPanel answerPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerPanel2.setOpaque(false);
        radio2 = new JRadioButton();
        radio2.setOpaque(false);
        correctAnswerGroup.add(radio2);
        answerField2 = new JTextField(30);
        answerPanel2.add(radio2);
        answerPanel2.add(new JLabel("Antwort 2:"));
        answerPanel2.add(answerField2);
        answersPanel.add(answerPanel2);

        // Antwort 3
        JPanel answerPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerPanel3.setOpaque(false);
        radio3 = new JRadioButton();
        radio3.setOpaque(false);
        correctAnswerGroup.add(radio3);
        answerField3 = new JTextField(30);
        answerPanel3.add(radio3);
        answerPanel3.add(new JLabel("Antwort 3:"));
        answerPanel3.add(answerField3);
        answersPanel.add(answerPanel3);

        // Antwort 4
        JPanel answerPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerPanel4.setOpaque(false);
        radio4 = new JRadioButton();
        radio4.setOpaque(false);
        correctAnswerGroup.add(radio4);
        answerField4 = new JTextField(30);
        answerPanel4.add(radio4);
        answerPanel4.add(new JLabel("Antwort 4:"));
        answerPanel4.add(answerField4);
        answersPanel.add(answerPanel4);

        inputPanel.add(answersPanel);

        JPanel catPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        catPanel.setOpaque(false);
        catPanel.add(new JLabel("Kategorie:"));
        categoryComboBox = new JComboBox<>();
        updateCategoryComboBox();
        catPanel.add(categoryComboBox);
        inputPanel.add(catPanel);

        panel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        addQuestionButton = new JButton("Frage hinzufügen");
        buttonPanel.add(addQuestionButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        addQuestionButton.addActionListener(e -> {
            addQuestion();
            updateQuestionList();
        });

        JPanel deletePanel = new JPanel(new BorderLayout());
        deletePanel.setOpaque(false);
        questionListModel = new DefaultListModel<>();
        questionList = new JList<>(questionListModel);
        JScrollPane questionListScroll = new JScrollPane(questionList);
        questionListScroll.setBorder(BorderFactory.createTitledBorder("Vorhandene Fragen"));
        deletePanel.add(questionListScroll, BorderLayout.CENTER);
        deleteQuestionButton = new JButton("Frage löschen");
        deleteQuestionButton.addActionListener(e -> deleteSelectedQuestion());
        deletePanel.add(deleteQuestionButton, BorderLayout.SOUTH);
        panel.add(deletePanel, BorderLayout.SOUTH);

        updateQuestionList();
        return panel;
    }

    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputFieldPanel.setOpaque(false);
        inputFieldPanel.add(new JLabel("Kategorie Name:"));
        categoryNameField = new JTextField(20);
        inputFieldPanel.add(categoryNameField);
        inputPanel.add(inputFieldPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        addCategoryButton = new JButton("Kategorie hinzufügen");
        addCategoryButton.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(addCategoryButton);
        inputPanel.add(buttonPanel);

        panel.add(inputPanel, BorderLayout.NORTH);

        addCategoryButton.addActionListener(e -> {
            String catName = categoryNameField.getText().trim();
            if (catName.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Bitte geben Sie einen Kategorienamen ein.", "Fehlende Angabe", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedXmlDocument == null) {
                JOptionPane.showMessageDialog(panel, "Es wurde noch keine XML-Datei ausgewählt.", "Keine Datei", JOptionPane.WARNING_MESSAGE);
                return;
            }
            addCategoryToSelectedXml(catName);
            categoryNameField.setText("");
            updateFileCategoryList();
            updateCategoryComboBox();
            updateCategoryDeletionList();
        });

        JPanel deletePanel = new JPanel(new BorderLayout());
        deletePanel.setOpaque(false);
        categoryDeletionListModel = new DefaultListModel<>();
        categoryDeletionList = new JList<>(categoryDeletionListModel);
        JScrollPane catListScroll = new JScrollPane(categoryDeletionList);
        catListScroll.setBorder(BorderFactory.createTitledBorder("Vorhandene Kategorien"));
        deletePanel.add(catListScroll, BorderLayout.CENTER);
        deleteCategoryButton = new JButton("Kategorie löschen");
        deleteCategoryButton.addActionListener(e -> deleteSelectedCategory());
        deletePanel.add(deleteCategoryButton, BorderLayout.SOUTH);
        panel.add(deletePanel, BorderLayout.SOUTH);

        updateCategoryDeletionList();
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        xmlFileListModel = new DefaultListModel<>();
        xmlFileList = new JList<>(xmlFileListModel);
        xmlFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane fileListScroll = new JScrollPane(xmlFileList);
        fileListScroll.setBorder(BorderFactory.createTitledBorder("XML Dateien"));
        fileListScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        panel.add(fileListScroll);

        panel.add(Box.createVerticalStrut(10));

        fileCategoryListModel = new DefaultListModel<>();
        fileCategoryList = new JList<>(fileCategoryListModel);
        JScrollPane categoryListScroll = new JScrollPane(fileCategoryList);
        categoryListScroll.setBorder(BorderFactory.createTitledBorder("Kategorien aus Datei"));
        categoryListScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        panel.add(categoryListScroll);

        panel.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        createNewXmlButton = new JButton("Neue XML-Datei erstellen");
        saveSelectedXmlButton = new JButton("XML speichern");
        deleteXmlButton = new JButton("XML löschen");
        bottomPanel.add(createNewXmlButton);
        bottomPanel.add(saveSelectedXmlButton);
        bottomPanel.add(deleteXmlButton);
        panel.add(bottomPanel);

        finishQuestionButton = new JButton("Fertig");
        finishQuestionButton.addActionListener(e -> mainFrame.switchLobbyStartPanel());
        JPanel finishPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        finishPanel.setOpaque(false);
        finishPanel.add(finishQuestionButton);
        panel.add(finishPanel);

        // Listener für XML-Dateiauswahl
        xmlFileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String fileName = xmlFileList.getSelectedValue();
                if (fileName != null) {
                    File file = new File(resourcesDir, fileName);
                    loadSelectedXmlFile(file);
                }
            }
        });

        saveSelectedXmlButton.addActionListener(e -> saveSelectedXmlFile());
        createNewXmlButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(resourcesDir);
            int option = fileChooser.showSaveDialog(CreateQuizPanel.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document newDoc = builder.newDocument();
                    Element newRoot = newDoc.createElement("database");
                    newDoc.appendChild(newRoot);
                    Element newKategorien = newDoc.createElement("Kategorien");
                    newRoot.appendChild(newKategorien);
                    Element newFragen = newDoc.createElement("Fragen");
                    newRoot.appendChild(newFragen);
                    Element newLoesungen = newDoc.createElement("Lösungen");
                    newRoot.appendChild(newLoesungen);
                    quizDocument = newDoc;
                    selectedXmlDocument = newDoc;
                    selectedXmlFile = file;
                    autoSaveXmlFile();
                    JOptionPane.showMessageDialog(CreateQuizPanel.this, "Neue XML-Datei wurde erstellt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                    populateXmlFileList();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CreateQuizPanel.this, "Fehler beim Erstellen der neuen XML-Datei: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteXmlButton.addActionListener(e -> deleteSelectedXmlFile());

        populateXmlFileList();
        return panel;
    }


    private void populateXmlFileList() {
        xmlFileListModel.clear();
        File dir = new File(resourcesDir);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".xml"));
            if (files != null) {
                for (File f : files) {
                    xmlFileListModel.addElement(f.getName());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Verzeichnis " + resourcesDir + " nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteSelectedXmlFile() {
        String selectedFileName = xmlFileList.getSelectedValue();
        if (selectedFileName != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Sind Sie sicher, dass Sie die XML-Datei \"" + selectedFileName + "\" löschen möchten?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                File file = new File(resourcesDir, selectedFileName);
                if (file.delete()) {
                    JOptionPane.showMessageDialog(this, "XML-Datei gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                    populateXmlFileList();
                    if (selectedXmlFile != null && selectedXmlFile.getName().equals(selectedFileName)) {
                        selectedXmlDocument = null;
                        selectedXmlFile = null;
                        fileCategoryListModel.clear();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Fehler beim Löschen der XML-Datei.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine XML-Datei aus.", "Fehlende Auswahl", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void loadSelectedXmlFile(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            selectedXmlDocument = builder.parse(file);
            selectedXmlDocument.getDocumentElement().normalize();
            selectedXmlFile = file;
            quizDocument = selectedXmlDocument;
            NodeList dbNodes = quizDocument.getElementsByTagName("database");
            if (dbNodes.getLength() > 0) {
                Element dbElement = (Element) dbNodes.item(0);
                kategorienElement = getFirstChildElement(dbElement, "Kategorien");
                fragenElement = getFirstChildElement(dbElement, "Fragen");
                loesungenElement = getFirstChildElement(dbElement, "Lösungen");
            }
            updateIDsFromDocument();
            updateFileCategoryList();
            updateCategoryComboBox();
            updateQuestionList();
            updateCategoryDeletionList();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der XML-Datei: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    private Element getFirstChildElement(Element parent, String tagName) {
        NodeList nl = parent.getElementsByTagName(tagName);
        return nl.getLength() > 0 ? (Element) nl.item(0) : null;
    }

    private void updateFileCategoryList() {
        fileCategoryListModel.clear();
        categoryMap.clear();
        if (selectedXmlDocument != null && kategorienElement != null) {
            NodeList katList = kategorienElement.getElementsByTagName("Kategorie");
            for (int i = 0; i < katList.getLength(); i++) {
                Element katElem = (Element) katList.item(i);
                String katID = katElem.getAttribute("KatID");
                Element nameElem = getFirstChildElement(katElem, "Name");
                if (nameElem != null) {
                    String katName = nameElem.getTextContent();
                    fileCategoryListModel.addElement(katName);
                    categoryMap.put(katName, katID);
                    try {
                        int idNum = Integer.parseInt(katID.replaceAll("\\D", ""));
                        if (idNum >= nextKatID) {
                            nextKatID = idNum + 1;
                        }
                    } catch (NumberFormatException ex) {
                        // ignorieren
                    }
                }
            }
        }
    }


    private void updateCategoryComboBox() {
        categoryComboBox.removeAllItems();
        if (selectedXmlDocument != null && kategorienElement != null) {
            NodeList katList = kategorienElement.getElementsByTagName("Kategorie");
            for (int i = 0; i < katList.getLength(); i++) {
                Element katElem = (Element) katList.item(i);
                Element nameElem = getFirstChildElement(katElem, "Name");
                if (nameElem != null) {
                    categoryComboBox.addItem(nameElem.getTextContent());
                }
            }
        } else if (kategorienElement != null) {
            NodeList katList = kategorienElement.getElementsByTagName("Kategorie");
            for (int i = 0; i < katList.getLength(); i++) {
                Element katElem = (Element) katList.item(i);
                Element nameElem = getFirstChildElement(katElem, "Name");
                if (nameElem != null) {
                    categoryComboBox.addItem(nameElem.getTextContent());
                }
            }
        }
    }

    private void updateQuestionList() {
        if (questionListModel == null) return;
        questionListModel.clear();
        if (fragenElement != null) {
            NodeList frageList = fragenElement.getElementsByTagName("Frage");
            for (int i = 0; i < frageList.getLength(); i++) {
                Element frageElem = (Element) frageList.item(i);
                String fid = frageElem.getAttribute("FID");
                Element frageNameElem = getFirstChildElement(frageElem, "FrageName");
                String frageText = frageNameElem != null ? frageNameElem.getTextContent() : "";
                questionListModel.addElement(fid + ": " + frageText);
            }
        }
    }

    private void updateCategoryDeletionList() {
        if (categoryDeletionListModel == null) return;
        categoryDeletionListModel.clear();
        if (kategorienElement != null) {
            NodeList katList = kategorienElement.getElementsByTagName("Kategorie");
            for (int i = 0; i < katList.getLength(); i++) {
                Element katElem = (Element) katList.item(i);
                Element nameElem = getFirstChildElement(katElem, "Name");
                if (nameElem != null) {
                    categoryDeletionListModel.addElement(nameElem.getTextContent());
                }
            }
        }
    }

    private void addCategoryToSelectedXml(String categoryName) {
        if (kategorienElement == null) {
            kategorienElement = quizDocument.createElement("Kategorien");
            rootDatabase.appendChild(kategorienElement);
        }
        Element newKat = quizDocument.createElement("Kategorie");
        newKat.setAttribute("KatID", "K" + nextKatID++);
        Element nameElem = quizDocument.createElement("Name");
        nameElem.appendChild(quizDocument.createTextNode(categoryName));
        newKat.appendChild(nameElem);
        kategorienElement.appendChild(newKat);
        JOptionPane.showMessageDialog(this, "Kategorie \"" + categoryName + "\" wurde hinzugefügt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        autoSaveXmlFile();
    }

    private void addQuestion() {
        String questionText = questionTextArea.getText().trim();
        String ans1 = answerField1.getText().trim();
        String ans2 = answerField2.getText().trim();
        String ans3 = answerField3.getText().trim();
        String ans4 = answerField4.getText().trim();
        if (questionText.isEmpty() || ans1.isEmpty() || ans2.isEmpty() || ans3.isEmpty() || ans4.isEmpty() ||
                correctAnswerGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus und wählen Sie die korrekte Antwort.", "Fehlende Angaben", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Ermittlung der korrekten Antwort (hier nur exemplarisch, da nicht weiter genutzt)
        int correctIndex = -1;
        if (radio1.isSelected()) correctIndex = 1;
        else if (radio2.isSelected()) correctIndex = 2;
        else if (radio3.isSelected()) correctIndex = 3;
        else if (radio4.isSelected()) correctIndex = 4;

        String selectedCatName = (String) categoryComboBox.getSelectedItem();
        if (selectedCatName == null || !categoryMap.containsKey(selectedCatName)) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine gültige Kategorie aus.", "Fehlende Kategorie", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String catID = categoryMap.get(selectedCatName);

        Element frageElem = quizDocument.createElement("Frage");
        frageElem.setAttribute("FID", "F" + nextFID++);
        frageElem.setAttribute("KID", catID);

        Element frageNameElem = quizDocument.createElement("FrageName");
        frageNameElem.appendChild(quizDocument.createTextNode(questionText));
        frageElem.appendChild(frageNameElem);

        Element antwortAElem = quizDocument.createElement("AntwortA");
        antwortAElem.appendChild(quizDocument.createTextNode(ans1));
        frageElem.appendChild(antwortAElem);

        Element antwortBElem = quizDocument.createElement("AntwortB");
        antwortBElem.appendChild(quizDocument.createTextNode(ans2));
        frageElem.appendChild(antwortBElem);

        Element antwortCElem = quizDocument.createElement("AntwortC");
        antwortCElem.appendChild(quizDocument.createTextNode(ans3));
        frageElem.appendChild(antwortCElem);

        Element antwortDElem = quizDocument.createElement("AntwortD");
        antwortDElem.appendChild(quizDocument.createTextNode(ans4));
        frageElem.appendChild(antwortDElem);

        if (fragenElement == null) {
            fragenElement = quizDocument.createElement("Fragen");
            rootDatabase.appendChild(fragenElement);
        }
        fragenElement.appendChild(frageElem);

        JOptionPane.showMessageDialog(this, "Frage hinzugefügt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        autoSaveXmlFile();

        // Felder zurücksetzen
        questionTextArea.setText("");
        answerField1.setText("");
        answerField2.setText("");
        answerField3.setText("");
        answerField4.setText("");
        correctAnswerGroup.clearSelection();
    }


    private void deleteSelectedQuestion() {
        String selectedValue = questionList.getSelectedValue();
        if (selectedValue == null) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Frage aus.", "Keine Auswahl", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String fid = selectedValue.split(":")[0].trim();
        NodeList frageList = fragenElement.getElementsByTagName("Frage");
        boolean found = false;
        for (int i = 0; i < frageList.getLength(); i++) {
            Element frageElem = (Element) frageList.item(i);
            if (frageElem.getAttribute("FID").equals(fid)) {
                fragenElement.removeChild(frageElem);
                found = true;
                break;
            }
        }
        if (found) {
            JOptionPane.showMessageDialog(this, "Frage gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            autoSaveXmlFile();
            updateQuestionList();
        } else {
            JOptionPane.showMessageDialog(this, "Frage nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedCategory() {
        String selectedCategory = categoryDeletionList.getSelectedValue();
        if (selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Kategorie aus.", "Keine Auswahl", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String katID = categoryMap.get(selectedCategory);
        if (katID == null) {
            JOptionPane.showMessageDialog(this, "Kategorie-ID nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie die Kategorie \"" + selectedCategory + "\" und alle zugehörigen Fragen löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        // Lösche die Kategorie
        NodeList katList = kategorienElement.getElementsByTagName("Kategorie");
        boolean found = false;
        for (int i = 0; i < katList.getLength(); i++) {
            Element katElem = (Element) katList.item(i);
            if (katElem.getAttribute("KatID").equals(katID)) {
                kategorienElement.removeChild(katElem);
                found = true;
                break;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "Kategorie nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Lösche alle Fragen, die dieser Kategorie zugeordnet sind
        NodeList frageList = fragenElement.getElementsByTagName("Frage");
        for (int i = frageList.getLength() - 1; i >= 0; i--) {
            Element frageElem = (Element) frageList.item(i);
            if (frageElem.getAttribute("KID").equals(katID)) {
                fragenElement.removeChild(frageElem);
            }
        }
        JOptionPane.showMessageDialog(this, "Kategorie und zugehörige Fragen gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        autoSaveXmlFile();
        updateCategoryComboBox();
        updateFileCategoryList();
        updateQuestionList();
        updateCategoryDeletionList();
    }

    private void updateIDsFromDocument() {
        nextFID = 1;
        if (fragenElement != null) {
            NodeList frageList = fragenElement.getElementsByTagName("Frage");
            for (int i = 0; i < frageList.getLength(); i++) {
                Element frageElem = (Element) frageList.item(i);
                String fid = frageElem.getAttribute("FID");
                try {
                    int idNum = Integer.parseInt(fid.replaceAll("\\D", ""));
                    if (idNum >= nextFID) {
                        nextFID = idNum + 1;
                    }
                } catch(NumberFormatException ex) {
                    // ignorieren
                }
            }
        }
        nextKatID = 1;
        if (kategorienElement != null) {
            NodeList katList = kategorienElement.getElementsByTagName("Kategorie");
            for (int i = 0; i < katList.getLength(); i++) {
                Element katElem = (Element) katList.item(i);
                String katID = katElem.getAttribute("KatID");
                try {
                    int idNum = Integer.parseInt(katID.replaceAll("\\D", ""));
                    if (idNum >= nextKatID) {
                        nextKatID = idNum + 1;
                    }
                } catch(NumberFormatException ex) {
                    // ignorieren
                }
            }
        }
    }

    private void saveSelectedXmlFile() {
        if (selectedXmlDocument == null || selectedXmlFile == null) {
            JOptionPane.showMessageDialog(this, "Keine XML-Datei zum Speichern ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(selectedXmlDocument);
            StreamResult result = new StreamResult(selectedXmlFile);
            transformer.transform(source, result);
            JOptionPane.showMessageDialog(this, "XML-Datei erfolgreich gespeichert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        } catch (TransformerException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der XML-Datei: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void autoSaveXmlFile() {
        if (selectedXmlDocument == null || selectedXmlFile == null) {
            return;
        }
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(selectedXmlDocument);
            StreamResult result = new StreamResult(selectedXmlFile);
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 150, 199, 255), 0, height, new Color(144, 224, 239));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
    }
}
