package protocol.messages;

public class RoundResultMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String categoryName;
    private int leftScore;
    private int rightScore;
    private String winner; // "LEFT", "RIGHT" oder "TIE"

    public RoundResultMessage(String categoryName, int leftScore, int rightScore, String winner) {
        this.categoryName = categoryName;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
        this.winner = winner;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public MessageType getType() {
        return MessageType.ROUND_RESULT;
    }
}
