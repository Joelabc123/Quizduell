package protocol.messages;

import java.util.List;

public class LobbyStatusMessage extends Message {
    private static final long serialVersionUID = 1L;

    private List<String> players;

    public LobbyStatusMessage(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    @Override
    public MessageType getType() {
        return MessageType.LOBBY_STATUS;
    }
}
