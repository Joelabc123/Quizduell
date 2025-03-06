package protocol;

import server.Answer;

import java.io.Serializable;
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
    public Question(String fid, String question, Answer correctAnswer) {
        this.fid = fid;
        this.frageName = question;
        this.correctAnswer = correctAnswer;
        this.answers = null;
    }

    public Question(String fid, String question) {
        this.fid = fid;
        this.frageName = question;
        this.correctAnswer = null;
        this.answers = null;
    }

    public boolean isCorrect(Answer answer) {
        return correctAnswer.equals(answer);
    }

    public String getFid() {
        return fid;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
