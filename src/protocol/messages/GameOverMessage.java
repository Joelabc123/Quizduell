package protocol.messages;

import server.GameState;

public class GameOverMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public GameOverMessage(GameState gameState) {
        super(MessageType.GAME_OVER);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.GAME_OVER;
    }
}
