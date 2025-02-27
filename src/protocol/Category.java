package protocol;

import java.util.ArrayList;

public class Category {
    private final String katID;
    private final String name;
    private ArrayList<Question> questions;

    public Category(String katID, String name) {
        this.katID = katID;
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public Question getRandQuestion() {
        Question question = questions.get((int) (Math.random() * questions.size()));

        question.setCorrectAnswer(null);

        return question;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }

    public String getKatID() {
        return katID;
    }
}
