package protocol.messages;

import java.util.UUID;

public class JoinLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    private int lobbyCode;

    public JoinLobbyMessage(int lobbyCode) {
        super(MessageType.JOIN_LOBBY);
        this.lobbyCode = lobbyCode;
    }

    public int getLobbyCode() {
        return lobbyCode;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_LOBBY;
    }
}
