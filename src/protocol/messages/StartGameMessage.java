package protocol.messages;

public class StartGameMessage extends Message {
    private static final long serialVersionUID = 1L;

    public StartGameMessage() {}

    @Override
    public MessageType getType() {
        return MessageType.START_GAME;
    }
}
