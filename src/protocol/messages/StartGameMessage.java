package protocol.messages;

import server.GameState;

public class StartGameMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public StartGameMessage(GameState gameState) {
        super(MessageType.START_GAME);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.START_GAME;
    }
}
