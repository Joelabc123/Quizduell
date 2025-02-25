package protocol;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final String katID;
    private final String name;
    private List<Question> questions;

    public Category(String katID, String name) {
        this.katID = katID;
        this.name = name;
        this.questions = new ArrayList<>();
    }

    private Category(Category category, List<Question> questions) {
        this.katID = category.katID;
        this.name = category.name;
        this.questions = questions;
    }

    public Question getRandQuestion() {
        Question question = questions.get((int) (Math.random() * questions.size()));

        question.setCorrectAnswer(null);

        return question;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }
}
