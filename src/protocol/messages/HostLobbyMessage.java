package protocol.messages;

public class HostLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    public HostLobbyMessage() {
    }

    @Override
    public MessageType getType() {
        return MessageType.HOST_LOBBY;
    }
}
