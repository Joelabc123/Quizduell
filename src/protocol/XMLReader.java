package protocol;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLReader {
    private Document document;

    /**
     * Konstruktor: Liest die XML-Datei ein.
     * @param filename Pfad zur XML-Datei.
     * @throws Exception falls ein Fehler beim Einlesen auftritt.
     */
    public XMLReader(String filename) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        document = builder.build(new File(filename));
    }

    /**
     * Liest alle Kategorien aus der XML-Datei aus.
     * @return Liste der Category-Objekte.
     */
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Element root = document.getRootElement(); // "database"
        Element kategorienElement = root.getChild("Kategorien");
        List<Element> kategorieList = kategorienElement.getChildren("Kategorie");
        for (Element k : kategorieList) {
            String katID = k.getAttributeValue("KatID");
            String name = k.getChildText("Name");
            categories.add(new Category(katID, name));
        }
        return categories;
    }

    /**
     * Liest alle Fragen aus der XML-Datei aus.
     * @return Liste der Question-Objekte.
     */
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        Element root = document.getRootElement();
        Element fragenElement = root.getChild("Fragen");
        List<Element> frageList = fragenElement.getChildren("Frage");
        for (Element f : frageList) {
            String fid = f.getAttributeValue("FID");
            String kid = f.getAttributeValue("KID");
            String frageName = f.getChildText("FrageName");
            String antwortA = f.getChildText("AntwortA");
            String antwortB = f.getChildText("AntwortB");
            String antwortC = f.getChildText("AntwortC");
            String antwortD = f.getChildText("AntwortD");

            Answer a = new Answer('A', antwortA);
            Answer b = new Answer('B', antwortB);
            Answer c = new Answer('C', antwortC);
            Answer d = new Answer('D', antwortD);

            // Zunächst ohne korrekte Antwort; diese wird in assignSolutions gesetzt.
            Question question = new Question(fid, kid, frageName, a, b, c, d);
            questions.add(question);
        }
        return questions;
    }

    /**
     * Liest alle Lösungseinträge aus der XML-Datei und weist den Fragen die korrekte Antwort zu.
     * @param questions Liste der bereits gelesenen Question-Objekte.
     */
    public void assignSolutions(List<Question> questions) {
        Element root = document.getRootElement();
        Element loesungenElement = root.getChild("Lösungen");
        List<Element> loesungList = loesungenElement.getChildren("Loesung");
        for (Element l : loesungList) {
            String fid = l.getAttributeValue("FID");
            String loesungText = l.getText().trim();
            for (Question q : questions) {
                if(q.getFid().equals(fid)) {
                    // Vergleiche den Lösungstext mit den Antworttexten
                    if(q.getAnswerA().getText().trim().equals(loesungText)) {
                        q.setCorrectAnswer('A');
                    } else if(q.getAnswerB().getText().trim().equals(loesungText)) {
                        q.setCorrectAnswer('B');
                    } else if(q.getAnswerC().getText().trim().equals(loesungText)) {
                        q.setCorrectAnswer('C');
                    } else if(q.getAnswerD().getText().trim().equals(loesungText)) {
                        q.setCorrectAnswer('D');
                    }
                    break;
                }
            }
        }
    }

    /**
     * Ordnet die Fragen ihren Kategorien zu.
     * @param categories Liste der Category-Objekte.
     * @param questions Liste der Question-Objekte.
     */
    public void assignQuestionsToCategories(List<Category> categories, List<Question> questions) {
        Map<String, Category> map = new HashMap<>();
        for (Category c : categories) {
            map.put(c.getKatID(), c);
        }
        for (Question q : questions) {
            Category c = map.get(q.getKid());
            if(c != null) {
                c.addQuestion(q);
            }
        }
    }
}
