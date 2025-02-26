package protocol.messages;

import server.GameState;

public class UpdateGameMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public UpdateGameMessage(GameState gameState) {
        super(MessageType.UPDATE_GAME);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.UPDATE_GAME;
    }
}
