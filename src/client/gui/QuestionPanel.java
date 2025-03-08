package client.gui;

import server.Answer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    public QuestionPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30,144,255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel headerLabel = new JLabel("Beantworte 3 Fragen", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("10", SwingConstants.RIGHT);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(timerLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        questionLabel = new JLabel("Frage 1: Was ist ...?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(questionLabel, BorderLayout.CENTER);

        JPanel answersPanel = new JPanel(new GridLayout(2,2,10,10));
        answersPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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
        questionLabel.setText("Frage 1: Was ist ...?");
        enableAndResetButtons();
        startTimer();
    }

    // Neues createAnswerButton, das den Text und den zugehörigen Answer-Wert übernimmt
    private JButton createAnswerButton(String text, Answer answerOption) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(30,144,255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdownTimer != null && countdownTimer.isRunning()) {
                    countdownTimer.stop();
                }
                // Prüfe, ob der übergebene Answer-Wert korrekt ist
                if (answerOption.equals(correctAnswer)) {
                    btn.setBackground(Color.GREEN);
                } else {
                    btn.setBackground(Color.RED);
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

        Color initialBlue = new Color(30,144,255);
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

    private void autoFail() {
        disableAllButtons();
        answerButton1.setBackground(Color.RED);
        answerButton2.setBackground(Color.RED);
        answerButton3.setBackground(Color.RED);
        answerButton4.setBackground(Color.RED);

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
            questionLabel.setText("Frage " + (questionCount + 1) + ": Was ist ...?");
            enableAndResetButtons();
            startTimer();
        } else {
            if (!roundCompleted) {
                roundCompleted = true;
                System.out.println("Round abgeschlossen. Externe Logik muss questionsCompleted() aufrufen.");
            }
        }
    }

    // Öffentliche Setter-Methoden für externe Steuerung:

    /**
     * Setzt den Fragetext, der im Frage-Label angezeigt wird.
     * @param questionText Der Text der Frage.
     */
    public void setQuestionText(String questionText) {
        questionLabel.setText(questionText);
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
}
