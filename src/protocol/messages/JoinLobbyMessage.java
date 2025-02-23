package protocol.messages;

public class JoinLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String lobbyId;
    private String username;

    public JoinLobbyMessage(String lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_LOBBY;
    }
}
