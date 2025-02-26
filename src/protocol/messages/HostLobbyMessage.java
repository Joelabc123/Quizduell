package protocol.messages;

import java.util.UUID;

public class HostLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    private UUID userId;

    public HostLobbyMessage(UUID userId) {
        super(MessageType.HOST_LOBBY);
        this.userId = userId;
    }

    @Override
    public MessageType getType() {
        return MessageType.HOST_LOBBY;
    }
}
