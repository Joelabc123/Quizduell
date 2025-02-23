package protocol.messages;

public class ErrorMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String errorMessage;
    private ErrorType errorType;

    public ErrorMessage(String errorMessage, ErrorType errorType) {
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public MessageType getType() {
        return MessageType.ERROR;
    }
}
