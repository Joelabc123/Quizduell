package protocol;

public class Round {
    private String categoryName;
    private int leftScore;
    private int rightScore;
    private String winner; // "LEFT", "RIGHT" oder "TIE"

    public Round(String categoryName, int leftScore, int rightScore) {
        this.categoryName = categoryName;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
        determineWinner();
    }

    private void determineWinner() {
        if (leftScore > rightScore) {
            winner = "LEFT";
        } else if (rightScore > leftScore) {
            winner = "RIGHT";
        } else {
            winner = "TIE";
        }
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
    public String toString() {
        return "Round{" +
                "categoryName='" + categoryName + '\'' +
                ", leftScore=" + leftScore +
                ", rightScore=" + rightScore +
                ", winner='" + winner + '\'' +
                '}';
    }
}
