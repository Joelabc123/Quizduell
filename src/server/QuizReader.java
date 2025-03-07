package server;

import protocol.Category;
import protocol.XMLReader;

import java.util.ArrayList;

public class QuizReader {

    public ArrayList<Category> categories = new ArrayList<>();
    private XMLReader xmlReader;

    public QuizReader() {
        try {
            xmlReader = new XMLReader("resources/quiz.xml");
            categories = xmlReader.parseCategory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Category> getEmptyCategories() {
        ArrayList<Category> emptyCategories = new ArrayList<>();

        for (Category category : categories) {
            emptyCategories.add(new Category(category.getKatID(), category.getName()));
        }

        return emptyCategories;
    }
}
