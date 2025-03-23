package protocol.messages;
public class SendQuizsetMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String quizset;

    public SendQuizsetMessage(String quizset) {
        super(MessageType.SEND_QUIZSET);
        this.quizset = quizset;
    }

    @Override
    public MessageType getType() {
        return MessageType.SEND_QUIZSET;
    }

    public String getQuizset() {
        return quizset;
    }
}
