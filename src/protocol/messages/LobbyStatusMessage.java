package protocol.messages;

import java.util.List;

public class LobbyStatusMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String lobbyId;
    private List<String> players;

    public LobbyStatusMessage(String lobbyId, List<String> players) {
        this.players = players;
        this.lobbyId = lobbyId;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    @Override
    public MessageType getType() {
        return MessageType.LOBBY_STATUS;
    }
}
