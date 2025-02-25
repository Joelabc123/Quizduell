package protocol;


import java.util.ArrayList;
import java.util.UUID;

public class CategoryRound {

     private final Category category;
     private ArrayList<QuestionRound> questions = new ArrayList<>();

     private ArrayList<UUID> winner = new ArrayList<>();

     public CategoryRound(Category category, ArrayList<QuestionRound> questions) {
         this.category = category;
         this.questions = questions;
     }

    public Category getCategory() {
        return category;
    }

    public ArrayList<QuestionRound> getQuestions() {
       return questions;
    }
}
