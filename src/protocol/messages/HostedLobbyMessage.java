package protocol.messages;

import server.GameState;

public class HostedLobbyMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public HostedLobbyMessage(GameState gameState) {
        super(MessageType.HOSTED_LOBBY);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.HOSTED_LOBBY;
    }
}
