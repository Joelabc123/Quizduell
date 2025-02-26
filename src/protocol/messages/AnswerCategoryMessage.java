package protocol.messages;

import server.Answer;

import java.util.ArrayList;

public class AnswerCategoryMessage extends Message {
    private static final long serialVersionUID = 1L;

    Answer selectedAnswer;

    public AnswerCategoryMessage(Answer selectedAnswer) {
        super(MessageType.ANSWER_QUESTION);
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public MessageType getType() {return MessageType.ANSWER_QUESTION;}
}
