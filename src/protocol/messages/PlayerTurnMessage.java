package protocol.messages;

import server.GameState;

public class PlayerTurnMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public PlayerTurnMessage(GameState gameState) {
        super(MessageType.PLAYER_TURN);
        this.gameState = gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_TURN;
    }

    public GameState getGameState() {
        return gameState;
    }
}
