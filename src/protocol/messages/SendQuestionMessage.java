package protocol.messages;

import server.GameState;

public class SendQuestionMessage extends Message {
    private static final long serialVersionUID = 1L;

    private GameState gameState;

    public SendQuestionMessage(GameState gameState) {
        super(MessageType.SEND_QUESTION);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public MessageType getType() {
        return MessageType.SEND_QUESTION;
    }

}
