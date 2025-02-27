package protocol;


import java.util.ArrayList;
import java.util.UUID;

public class CategoryRound {


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
}
