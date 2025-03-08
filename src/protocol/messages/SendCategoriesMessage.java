package protocol.messages;

import server.GameState;

public class SendCategoriesMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public SendCategoriesMessage(GameState gameState) {
        super(MessageType.SEND_CATEGORIES);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.SEND_CATEGORIES;
    }
}
