package protocol.messages;

public class GameOverMessage extends Message {
    private static final long serialVersionUID = 1L;

    private int finalLeftScore;
    private int finalRightScore;
    private String overallWinner;

    public GameOverMessage(int finalLeftScore, int finalRightScore, String overallWinner) {
        this.finalLeftScore = finalLeftScore;
        this.finalRightScore = finalRightScore;
        this.overallWinner = overallWinner;
    }

    public int getFinalLeftScore() {
        return finalLeftScore;
    }

    public int getFinalRightScore() {
        return finalRightScore;
    }

    public String getOverallWinner() {
        return overallWinner;
    }

    @Override
    public MessageType getType() {
        return MessageType.GAME_OVER;
    }
}
