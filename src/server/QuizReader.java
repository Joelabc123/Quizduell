package server;

import protocol.Category;
import protocol.XMLReader;
import java.util.ArrayList;

public class QuizReader {

    public ArrayList<Category> categories = new ArrayList<>();
    private XMLReader xmlReader;

    // Neuer Konstruktor, der einen Dateipfad akzeptiert
    public QuizReader(String filePath) {
        try {
            xmlReader = new XMLReader(filePath);
            categories = xmlReader.parseCategory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Default-Konstruktor, der die Standard-Quizset-Datei verwendet
    public QuizReader() {
        this("resources/quiz.xml");
    }

    public ArrayList<Category> getEmptyCategories() {
        ArrayList<Category> emptyCategories = new ArrayList<>();
        for (Category category : categories) {
            emptyCategories.add(new Category(category.getKatID(), category.getName()));
        }
        return emptyCategories;
    }
}
