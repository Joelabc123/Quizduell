package client.gui;

import server.Answer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class QuestionPanel extends JPanel {

    private MainGameFrame mainFrame;
    private JLabel questionLabel;
    private JButton answerButton1, answerButton2, answerButton3, answerButton4;
    private int questionCount = 0;
    private final int MAX_QUESTIONS = 3;

    // Platzhalter: Aktuelle Kategorie und korrekte Antwort als Enum
    private String currentCategory = "Dummy Kat";
    private Answer correctAnswer = Answer.ANSWER_A;

    // Timer-Elemente
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeRemaining; // in Sekunden

    // Flag, um Mehrfachaufrufe des Rundenabschlusses zu verhindern
    private boolean roundCompleted = false;

    // ArrayList zum Sammeln der Antworten
    private ArrayList<Boolean> answers = new ArrayList<>();

    public QuestionPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        setOpaque(false); // Damit der benutzerdefinierte Hintergrund sichtbar bleibt
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header-Panel: Enthält Banner mit Überschrift und Countdown
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(true);
        headerPanel.setBackground(new Color(0, 119, 182)); // #0077B6
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Beantworte 3 Fragen", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Größer und bold
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("10", SwingConstants.RIGHT);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Größer und bold
        headerPanel.add(timerLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Frage-Label: Hier wird der Fragetext angezeigt – HTML für korrekte Formatierung
        questionLabel = new JLabel("<html>Frage 1: Was ist ...?</html>", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        questionLabel.setForeground(Color.WHITE);
        add(questionLabel, BorderLayout.CENTER);

        // Panel für Antwort-Buttons
        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        answersPanel.setOpaque(false);
        answersPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        answerButton1 = createAnswerButton("Antwort A", Answer.ANSWER_A);
        answerButton2 = createAnswerButton("Antwort B", Answer.ANSWER_B);
        answerButton3 = createAnswerButton("Antwort C", Answer.ANSWER_C);
        answerButton4 = createAnswerButton("Antwort D", Answer.ANSWER_D);

        answersPanel.add(answerButton1);
        answersPanel.add(answerButton2);
        answersPanel.add(answerButton3);
        answersPanel.add(answerButton4);

        add(answersPanel, BorderLayout.SOUTH);
    }

    public void resetRound() {
        questionCount = 0;
        roundCompleted = false;
        setQuestionText("Frage 1: Was ist ...?");
        answers.clear(); // Alte Antworten löschen
        enableAndResetButtons();
        startTimer();
    }

    // Erzeugt einen Antwortbutton mit 3D-Effekt, erweitertem Padding und dynamischem Hover
    private JButton createAnswerButton(String text, Answer answerOption) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Color baseColor = new Color(30, 144, 255);
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        // Erweitertes Padding
        btn.setMargin(new Insets(10, 20, 10, 20));
        // 3D-Effekt: CompoundBorder aus BevelBorder und weißem LineBorder
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
        // Dynamischer Hover-Effekt
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdownTimer != null && countdownTimer.isRunning()) {
                    countdownTimer.stop();
                }
                System.out.println("Button clicked: " + answerOption + ", correctAnswer: " + correctAnswer);
                if (answerOption.equals(correctAnswer)) {
                    btn.setBackground(Color.GREEN);
                    answers.add(true);
                } else {
                    btn.setBackground(Color.RED);
                    answers.add(false);
                }
                disableAllButtons();

                Timer delayTimer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        nextQuestion();
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            }
        });
        return btn;
    }

    private void disableAllButtons() {
        answerButton1.setEnabled(false);
        answerButton2.setEnabled(false);
        answerButton3.setEnabled(false);
        answerButton4.setEnabled(false);
    }

    private void enableAndResetButtons() {
        answerButton1.setEnabled(true);
        answerButton2.setEnabled(true);
        answerButton3.setEnabled(true);
        answerButton4.setEnabled(true);
        Color initialBlue = new Color(30, 144, 255);
        answerButton1.setBackground(initialBlue);
        answerButton2.setBackground(initialBlue);
        answerButton3.setBackground(initialBlue);
        answerButton4.setBackground(initialBlue);
    }

    private void startTimer() {
        timeRemaining = 10;
        timerLabel.setText(String.valueOf(timeRemaining));
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText(String.valueOf(timeRemaining));
                if (timeRemaining <= 0) {
                    countdownTimer.stop();
                    autoFail();
                }
            }
        });
        countdownTimer.setInitialDelay(0);
        countdownTimer.start();
    }

    // Falls keine Antwort gewählt wurde, wird automatisch ein "Fail" registriert
    private void autoFail() {
        disableAllButtons();
        answerButton1.setBackground(Color.RED);
        answerButton2.setBackground(Color.RED);
        answerButton3.setBackground(Color.RED);
        answerButton4.setBackground(Color.RED);
        // Füge explizit eine "falsche" Antwort hinzu, wenn der Timer abläuft
        answers.add(false);
        Timer delayTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextQuestion();
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void nextQuestion() {
        questionCount++;
        if (questionCount < MAX_QUESTIONS) {
            mainFrame.getClientHandler().gameManager.setQuestions();
            enableAndResetButtons();
            startTimer();
        } else {
            if (!roundCompleted) {
                roundCompleted = true;
                System.out.println("Round abgeschlossen. Externe Logik muss questionsCompleted() aufrufen.");
                System.out.println("Antworten: " + answers);
                mainFrame.getClientHandler().gameManager.answerQuestion(answers);
                answers.clear();
                System.out.println("Antworten nach clear(): " + answers);
            }
        }
    }

    // Öffentliche Setter-Methoden für externe Steuerung:

    /**
     * Setzt den Fragetext, der im Frage-Label angezeigt wird.
     * Der Text wird in HTML umgewandelt, um korrekte Formatierung zu ermöglichen.
     * @param questionText Der Text der Frage.
     */
    public void setQuestionText(String questionText) {
        questionLabel.setText("<html>" + questionText + "</html>");
    }

    /**
     * Setzt den Kategorienamen für diese Frage.
     * @param category Der Name der Kategorie.
     */
    public void setCategory(String category) {
        this.currentCategory = category;
    }

    /**
     * Setzt die korrekte Antwort, die zur Auswertung der gedrückten Antwortbuttons verwendet wird.
     * @param correctAnswer Der korrekte Answer (z. B. Answer.ANSWER_A).
     */
    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Setzt alle vier Antwortmöglichkeiten gleichzeitig.
     * @param optionA Text für Antwort A.
     * @param optionB Text für Antwort B.
     * @param optionC Text für Antwort C.
     * @param optionD Text für Antwort D.
     */
    public void setAnswerOptions(String optionA, String optionB, String optionC, String optionD) {
        answerButton1.setText(optionA);
        answerButton2.setText(optionB);
        answerButton3.setText(optionC);
        answerButton4.setText(optionD);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Hintergrund mit Farbverlauf: von hellblau oben zu dunkelblau unten
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(0,150,199,255), 0, height, new Color(144,224,239));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        // Kein Aufruf von super.paintComponent(g) damit der Gradient erhalten bleibt
    }
}
