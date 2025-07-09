package protocol;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import server.Answer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLReader {

    private Document document;

    public XMLReader(String filename) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        document = builder.build(new File(filename));
    }

    public ArrayList<Category> parseCategory() {
        //Root-Element und Kategorien-Element einlesen
        Element root = document.getRootElement(); // z.B. <database>
        Element kategorienElem = root.getChild("Kategorien");
        List<Element> kategorieElems = kategorienElem.getChildren("Kategorie");
        ArrayList<Category> categories = new ArrayList<>();
        Map<String, Category> categoryMap = new HashMap<>();

        //Für jedes Kategorie-Element, erstellen Sie ein Category-Objekt
        for (Element k : kategorieElems) {
            String katID = k.getAttributeValue("KatID");
            String name = k.getChildText("Name");
            Category cat = new Category(katID, name);
            categories.add(cat);
            categoryMap.put(katID, cat);
        }

        Element fragenElem = root.getChild("Fragen");
        List<Element> frageElems = fragenElem.getChildren("Frage");
        ArrayList<QuestionWrapper> questionWrappers = new ArrayList<>();

        for (Element f : frageElems) {
            String fid = f.getAttributeValue("FID");
            String kid = f.getAttributeValue("KID");  // Kategorie-ID
            String frageName = f.getChildText("FrageName");
            String antwortA = f.getChildText("AntwortA");
            String antwortB = f.getChildText("AntwortB");
            String antwortC = f.getChildText("AntwortC");
            String antwortD = f.getChildText("AntwortD");

            Map<Answer, String> answerMap = new HashMap<>();
            answerMap.put(Answer.ANSWER_A, antwortA);
            answerMap.put(Answer.ANSWER_B, antwortB);
            answerMap.put(Answer.ANSWER_C, antwortC);
            answerMap.put(Answer.ANSWER_D, antwortD);

            Question question = new Question(fid, frageName, answerMap, Answer.ANSWER_A);
            QuestionWrapper qw = new QuestionWrapper(question, kid, antwortA, antwortB, antwortC, antwortD);
            questionWrappers.add(qw);
        }

        Element loesungenElem = root.getChild("Lösungen");
        List<Element> loesungElems = loesungenElem.getChildren("Loesung");
        Map<String, String> solutionMap = new HashMap<>();

        for (Element l : loesungElems) {
            String fid = l.getAttributeValue("FID");
            String solutionText = l.getText().trim();
            solutionMap.put(fid, solutionText);
        }

        for (QuestionWrapper qw : questionWrappers) {
            String solText = solutionMap.get(qw.question.getFid());
            if (solText != null) {
                if (solText.equals(qw.antwortA.trim())) {
                    qw.question.setCorrectAnswer(Answer.ANSWER_A);
                } else if (solText.equals(qw.antwortB.trim())) {
                    qw.question.setCorrectAnswer(Answer.ANSWER_B);
                } else if (solText.equals(qw.antwortC.trim())) {
                    qw.question.setCorrectAnswer(Answer.ANSWER_C);
                } else if (solText.equals(qw.antwortD.trim())) {
                    qw.question.setCorrectAnswer(Answer.ANSWER_D);
                } else {
                    System.err.println("Keine passende Lösung für Frage " + qw.question.getFid());
                }
            } else {
                System.err.println("Keine Lösung für Frage " + qw.question.getFid());
            }

            Category cat = categoryMap.get(qw.kid);
            if (cat != null) {
                cat.addQuestion(qw.question);
            } else {
                System.err.println("Kategorie " + qw.kid + " nicht gefunden für Frage " + qw.question.getFid());
            }
        }
        return categories;
    }

    private static class QuestionWrapper {
        public Question question;
        public String kid;
        public String antwortA;
        public String antwortB;
        public String antwortC;
        public String antwortD;

        public QuestionWrapper(Question question, String kid, String antwortA, String antwortB, String antwortC, String antwortD) {
            this.question = question;
            this.kid = kid;
            this.antwortA = antwortA;
            this.antwortB = antwortB;
            this.antwortC = antwortC;
            this.antwortD = antwortD;
        }
    }
}
