package protocol.messages;

import java.util.ArrayList;

public class AnswerQuestionMessage extends Message {
    private static final long serialVersionUID = 1L;

    ArrayList<Boolean> answers;

    public AnswerQuestionMessage(ArrayList<Boolean> answers) {
        super(MessageType.ANSWER_QUESTION);
        this.answers = answers;
    }

    @Override
    public MessageType getType() {return MessageType.ANSWER_QUESTION;}

    public ArrayList<Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Boolean> answers) {
        this.answers = answers;
    }

    public int getScore() {
        int score = 0;
        for (Boolean answer : answers) {
            if (answer) {
                score++;
            }
        }
        return score;
    }
}
