package protocol.messages;

public class ErrorMessage extends Message {
    private static final long serialVersionUID = 1L;

    private ErrorType errorType;

    public ErrorMessage(ErrorType errorType) {
        super(MessageType.ERROR);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR;
    }
}
