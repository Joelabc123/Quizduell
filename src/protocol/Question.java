package protocol;

import server.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String fid;
    private final String frageName;

    private final Map<Answer, String> answers;
    private Answer correctAnswer;

    public Question(String fid, String question, Map<Answer, String> answers, Answer correctAnswer) {
        this.fid = fid;
        this.frageName = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;

        if(!answers.containsKey(correctAnswer)) {
            throw new IllegalArgumentException("Correct answer not in answers");
        }
    }

    public String getFid() {
        return fid;
    }

    public String getFrageName() {
        return frageName;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public ArrayList<String> getAnswerOptions() {
        ArrayList<String> options = new ArrayList<>();
        // Option 1: Reihenfolge anhand der Enum-Werte
        for (Answer key : Answer.values()) {
            System.out.println("Key: " + key);
            if (answers != null && answers.containsKey(key)) {
                options.add(answers.get(key));
                System.out.println("Options: " + options);
            }
            System.out.println("Size: " + options.size());
        }
        return options;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }
}
