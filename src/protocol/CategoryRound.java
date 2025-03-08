package protocol;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class CategoryRound implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Category category;

    private ArrayList<UUID> winner = new ArrayList<>();
    private ArrayList<QuestionRound> questionRounds = new ArrayList<>(); //3 questions

    public CategoryRound(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<QuestionRound> getQuestionRounds() {
        return questionRounds;
    }

    public void setQuestionRounds(ArrayList<QuestionRound> questionRounds) {
        this.questionRounds = questionRounds;
    }

    public void setWinner(ArrayList<UUID> winner) {
        this.winner = winner;
    }
}
