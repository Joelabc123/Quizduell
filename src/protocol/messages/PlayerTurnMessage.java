package protocol.messages;

import server.GameState;

public class PlayerTurnMessage extends Message {
    private static final long serialVersionUID = 1L;

    boolean playerTurn;

    public PlayerTurnMessage() {
        super(MessageType.PLAYER_TURN);
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_TURN;
    }

    public boolean getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }
}
