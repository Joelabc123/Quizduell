package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuestionPanel extends JPanel {

    private MainGameFrame mainFrame;
    private JLabel questionLabel;
    private JButton answerButton1, answerButton2, answerButton3, answerButton4;
    private int questionCount = 0;
    private final int MAX_QUESTIONS = 3;

    // Dummy-Platzhalter: Aktuelle Kategorie und korrekte Antwort (immer "Antwort A")
    private String currentCategory = "Sport";
    private String correctAnswer = "Antwort A";

    // Timer-Elemente
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeRemaining; // in Sekunden

    // Flag, um Mehrfachaufrufe des Rundenabschlusses zu verhindern
    private boolean roundCompleted = false;

    public QuestionPanel(MainGameFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        // Den Reset der Runde steuern wir nun explizit aus dem MainGameFrame
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header mit Titel und Timer
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

        // Frage-Label
        questionLabel = new JLabel("Frage 1: Was ist ...?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(questionLabel, BorderLayout.CENTER);

        // Panel für Antwort-Buttons
        JPanel answersPanel = new JPanel(new GridLayout(2,2,10,10));
        answersPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        answerButton1 = createAnswerButton("Antwort A");
        answerButton2 = createAnswerButton("Antwort B");
        answerButton3 = createAnswerButton("Antwort C");
        answerButton4 = createAnswerButton("Antwort D");

        answersPanel.add(answerButton1);
        answersPanel.add(answerButton2);
        answersPanel.add(answerButton3);
        answersPanel.add(answerButton4);

        add(answersPanel, BorderLayout.SOUTH);
    }

    /**
     * Wird aus dem MainGameFrame aufgerufen, bevor das QuestionPanel angezeigt wird.
     * Setzt den Frage-Zähler und den roundCompleted-Flag zurück, aktualisiert die Anzeige und startet den Timer.
     */
    public void resetRound() {
        questionCount = 0;
        roundCompleted = false;
        questionLabel.setText("Frage 1: Was ist ...?");
        enableAndResetButtons();
        startTimer();
    }

    private JButton createAnswerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(30,144,255)); // Quizduell-Blau
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(countdownTimer != null && countdownTimer.isRunning()){
                    countdownTimer.stop();
                }
                JButton clicked = (JButton)e.getSource();
                if(clicked.getText().equals(correctAnswer)){
                    clicked.setBackground(Color.GREEN);
                } else {
                    clicked.setBackground(Color.RED);
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

    private void disableAllButtons(){
        answerButton1.setEnabled(false);
        answerButton2.setEnabled(false);
        answerButton3.setEnabled(false);
        answerButton4.setEnabled(false);
    }

    private void enableAndResetButtons(){
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

    private void startTimer(){
        timeRemaining = 10;
        timerLabel.setText(String.valueOf(timeRemaining));

        countdownTimer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText(String.valueOf(timeRemaining));
                if(timeRemaining <= 0){
                    countdownTimer.stop();
                    autoFail();
                }
            }
        });
        countdownTimer.setInitialDelay(0);
        countdownTimer.start();
    }

    private void autoFail(){
        disableAllButtons();
        // Markiere alle Buttons als falsch (rot)
        answerButton1.setBackground(Color.RED);
        answerButton2.setBackground(Color.RED);
        answerButton3.setBackground(Color.RED);
        answerButton4.setBackground(Color.RED);

        Timer delayTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nextQuestion();
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void nextQuestion(){
        questionCount++;
        if(questionCount < MAX_QUESTIONS){
            questionLabel.setText("Frage " + (questionCount + 1) + ": Was ist ...?");
            enableAndResetButtons();
            startTimer();
        } else {
            // Letzte Frage der Runde
            if(!roundCompleted){
                roundCompleted = true;
                String winner = "LEFT"; // Dummy – hier durch echte Logik ersetzen
                mainFrame.questionsCompleted(currentCategory, winner);
            }
        }
    }
}
