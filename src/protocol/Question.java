package protocol;

import server.Answer;

import java.util.Map;

public class Question {
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
