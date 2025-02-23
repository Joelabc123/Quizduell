package protocol.messages;

public class ScoreUpdateMessage extends Message {
    private static final long serialVersionUID = 1L;

    private int leftScore;
    private int rightScore;

    public ScoreUpdateMessage(int leftScore, int rightScore) {
        this.leftScore = leftScore;
        this.rightScore = rightScore;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    @Override
    public MessageType getType() {
        return MessageType.SCORE_UPDATE;
    }
}
