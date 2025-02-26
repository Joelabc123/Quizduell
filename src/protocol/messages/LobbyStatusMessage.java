package protocol.messages;

import java.util.ArrayList;

public class LobbyStatusMessage extends Message {
    private static final long serialVersionUID = 1L;

    private int lobbyCode;

    public LobbyStatusMessage( int lobbyCode) {
        super(MessageType.LOBBY_STATUS);
        this.lobbyCode = lobbyCode;
    }

    public int getLobbyCode() {
        return lobbyCode;
    }

    @Override
    public MessageType getType() {
        return MessageType.LOBBY_STATUS;
    }
}
