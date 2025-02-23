package protocol.messages;

public class AnswerMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String questionId;
    private char selectedOption;  // 'A', 'B', 'C' oder 'D'
    private String username;

    public AnswerMessage(String questionId, char selectedOption, String username) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.username = username;
    }

    public String getQuestionId() {
        return questionId;
    }

    public char getSelectedOption() {
        return selectedOption;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public MessageType getType() {
        return MessageType.ANSWER;
    }
}
