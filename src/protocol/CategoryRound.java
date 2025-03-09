package protocol;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryRound implements Serializable {

    final static long serialVersionUID = 1L;

    private ArrayList<Question> questions;

    private final Category category;

    private ArrayList<Boolean> answersPlayerA = new ArrayList<>();
    private ArrayList<Boolean> answersPlayerB = new ArrayList<>();

    public CategoryRound(ArrayList <Question> questions, Category category) {
        this.questions = questions;
        this.category = category;
    }

    public CategoryRound(Category category) {
        this.category = category;
    }

    public void addQuestion(Question question) {
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

    public enum GameOutcome {
        PLAYER_A,
        PLAYER_B,
        DRAW
    }

    public GameOutcome getWinner() {
        int scoreA = 0;
        int scoreB = 0;
        for (Boolean answer : answersPlayerA) {
            if (answer) {
                scoreA++;
            }
        }
        for (Boolean answer : answersPlayerB) {
            if (answer) {
                scoreB++;
            }
        }
        if (scoreA > scoreB) {
            return GameOutcome.PLAYER_A;
        } else if (scoreA < scoreB) {
            return GameOutcome.PLAYER_B;
        } else {
            return GameOutcome.DRAW;
        }
    }

}