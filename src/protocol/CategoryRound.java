package protocol;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryRound implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Question> questions;
    private final Category category;

    private ArrayList<Boolean> answersPlayerA = new ArrayList<>();
    private ArrayList<Boolean> answersPlayerB = new ArrayList<>();

    private GameOutcome winner;

    public CategoryRound(ArrayList<Question> questions, Category category) {
        this.questions = questions;
        this.category = category;
    }

    public CategoryRound(Category category) {
        this.category = category;
        this.questions = new ArrayList<>();
    }

    // Kopierkonstruktor – erzeugt eine tiefe Kopie
    public CategoryRound(CategoryRound original) {
        this.category = original.category; // Annahme: Category ist unveränderlich oder wird zentral geteilt
        this.questions = new ArrayList<>(original.questions);
        this.answersPlayerA = new ArrayList<>(original.answersPlayerA);
        this.answersPlayerB = new ArrayList<>(original.answersPlayerB);
        this.winner = original.winner;
    }

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
    }

    public ArrayList<Boolean> getAnswersPlayerA() {
        return answersPlayerA;
    }

    public ArrayList<Boolean> getAnswersPlayerB() {
        return answersPlayerB;
    }

    public void setAnswersPlayerA(ArrayList<Boolean> answersPlayerA) {
        this.answersPlayerA = answersPlayerA;
    }

    public void setAnswersPlayerB(ArrayList<Boolean> answersPlayerB) {
        this.answersPlayerB = answersPlayerB;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public Category getCategory() {
        return category;
    }

    public void initializeQuestions() {
        if (questions == null) {
            System.out.println("questions is null");
            questions = new ArrayList<>();
        }
    }

    /**
     * Berechnet den Gewinner dieser Kategorie-Runde anhand der Antworten.
     */
    public void setWinner() {
        int scorePlayerA = 0;
        int scorePlayerB = 0;

        for(boolean b: answersPlayerA) {
            if(b) {
                scorePlayerA++;
            }
        }
        for(boolean b: answersPlayerB) {
            if(b) {
                scorePlayerB++;
            }
        }
        if(scorePlayerA == scorePlayerB) {
            winner = GameOutcome.DRAW;
        } else if(scorePlayerA > scorePlayerB) {
            winner = GameOutcome.PLAYER_A;
        } else {
            winner = GameOutcome.PLAYER_B;
        }
    }

    public GameOutcome getWinner() {
        return winner;
    }

}
