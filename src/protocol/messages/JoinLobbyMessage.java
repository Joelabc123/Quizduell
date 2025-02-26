package protocol.messages;

import java.util.UUID;

public class JoinLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    private UUID userId;
    private int lobbyCode;

    public JoinLobbyMessage(UUID userId, int lobbyCode) {
        super(MessageType.JOIN_LOBBY);
        this.userId = userId;
        this.lobbyCode = lobbyCode;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getLobbyCode() {
        return lobbyCode;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_LOBBY;
    }
}
