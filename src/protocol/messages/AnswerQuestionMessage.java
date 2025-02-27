package protocol.messages;

import server.Answer;

public class AnswerQuestionMessage extends Message {
    private static final long serialVersionUID = 1L;

    Answer selectedAnswer;
    String fId;

    public AnswerQuestionMessage(Answer selectedAnswer, String fId) {
        super(MessageType.ANSWER_QUESTION);
        this.selectedAnswer = selectedAnswer;
        this.fId = fId;
    }

    @Override
    public MessageType getType() {return MessageType.ANSWER_QUESTION;}

    public Answer getSelectedAnswer() {
        return selectedAnswer;
    }

    public String getfId() {
        return fId;
    }
}
