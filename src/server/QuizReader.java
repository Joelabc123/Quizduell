package server;

import protocol.Category;
import protocol.XMLReader;

import java.util.ArrayList;

public class QuizReader {

    private ArrayList<Category> categories = new ArrayList<>();

    public QuizReader() throws Exception {
        XMLReader quiz = new XMLReader("src/quiz.xml");


    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<Category> getThreeRandomCategories() {
        ArrayList<Category> randomCategories = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            randomCategories.add(categories.get((int) (Math.random() * categories.size())));
        }
        return randomCategories;
    }
}
