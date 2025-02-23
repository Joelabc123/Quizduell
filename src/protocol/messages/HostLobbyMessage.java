package protocol.messages;

public class HostLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String username;

    public HostLobbyMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public MessageType getType() {
        return MessageType.HOST_LOBBY;
    }
}
