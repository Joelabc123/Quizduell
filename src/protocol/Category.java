package protocol;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String katID;
    private String name;
    private List<Question> questions;

    public Category(String katID, String name) {
        this.katID = katID;
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public String getKatID() {
        return katID;
    }

    public void setKatID(String katID) {
        this.katID = katID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    @Override
    public String toString() {
        return "Category{" +
                "katID='" + katID + '\'' +
                ", name='" + name + '\'' +
                ", questions=" + questions.size() +
                '}';
    }
}
