package protocol;

import server.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestionRound {

    private Category category;
    private Question question;

    private Map<String, Answer> answerPlayerA = new HashMap<>(); //FID, Answer
    private Map<String, Answer> answerPlayerB = new HashMap<>();//FID, Answer

    public QuestionRound(Category category, Question question) {
        this.category = category;
        this.question = question;
    }

    public Category getCategory() {
        return category;
    }

    public void setAnswerPlayerA(String fid, Answer answer) {
        answerPlayerA.put(fid, answer);
    }

    public void setAnswerPlayerB(String fid, Answer answer) {
        answerPlayerB.put(fid, answer);
    }

    public Map<String, Answer> getAnswerPlayerA() {
        return answerPlayerA;
    }

    public Map<String, Answer> getAnswerPlayerB() {
        return answerPlayerB;
    }

}
