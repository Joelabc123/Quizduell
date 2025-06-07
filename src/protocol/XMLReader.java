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

    /**
     * Aufgabe 1:
     * Implementieren Sie den Konstruktor der Klasse XMLReader.
     *
     * 1.1. Nutzen Sie einen SAXBuilder, um die XML-Datei einzulesen.
     * 1.2. Speichern Sie das erstellte Document in dem Attribut "document".
     * 1.3. Achten Sie darauf, mögliche Exceptions korrekt weiterzuwerfen.
     */
    public XMLReader(String filename) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        document = builder.build(new File(filename));
    }

    /**
     * Aufgabe 2:
     * Implementieren Sie die Methode parseCategory(), die die XML-Datei parst und
     * eine ArrayList von Category-Objekten zurückgibt.
     *
     * Teilschritte:
     * 2.1. Lesen Sie das Root-Element und das Kind-Element <Kategorien> ein.
     * 2.2. Gehen Sie alle <Kategorie>-Elemente durch und extrahieren Sie jeweils
     *      die Attribute "KatID" und den Kind-Text "Name".
     * 2.3. Erzeugen Sie für jede Kategorie ein Category-Objekt, fügen Sie es einer Liste hinzu
     *      und speichern Sie es zusätzlich in einer Map (zur späteren Zuordnung).
     */
    public ArrayList<Category> parseCategory() {
        // Schritt 2.1: Root-Element und <Kategorien>-Element einlesen
        Element root = document.getRootElement(); // z.B. <database>
        Element kategorienElem = root.getChild("Kategorien");
        List<Element> kategorieElems = kategorienElem.getChildren("Kategorie");
        ArrayList<Category> categories = new ArrayList<>();
        Map<String, Category> categoryMap = new HashMap<>();

        // Aufgabe 2.2: Für jedes <Kategorie>-Element, erstellen Sie ein Category-Objekt
        for (Element k : kategorieElems) {
            String katID = k.getAttributeValue("KatID");
            String name = k.getChildText("Name");
            Category cat = new Category(katID, name);
            categories.add(cat);
            categoryMap.put(katID, cat);
        }

        /**
         * Aufgabe 3:
         * Erweitern Sie parseCategory() um das Einlesen der Fragen aus dem XML.
         *
         * Teilschritte:
         * 3.1. Lesen Sie das <Fragen>-Element und alle enthaltenen <Frage>-Elemente.
         * 3.2. Extrahieren Sie aus jedem <Frage>-Element die Attribute "FID" und "KID" sowie
         *      den Kind-Text "FrageName" und die Antworttexte aus den Elementen <AntwortA> bis <AntwortD>.
         * 3.3. Erstellen Sie eine Map, die die Antwortmöglichkeiten (Answer-Enums) mit den Texten verknüpft.
         * 3.4. Erzeugen Sie ein Question-Objekt, bei dem Sie zunächst einen Dummy-Wert als korrekte Antwort setzen.
         * 3.5. Verpacken Sie das Question-Objekt und die Originalantworttexte in einem Hilfsobjekt (QuestionWrapper)
         *      und speichern Sie dieses in einer Liste.
         */
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

        /**
         * Aufgabe 4:
         * Lesen Sie die Lösungen aus dem XML ein.
         *
         * Teilschritte:
         * 4.1. Lesen Sie das <Lösungen>-Element und alle <Loesung>-Elemente.
         * 4.2. Extrahieren Sie aus jedem <Loesung>-Element das Attribut "FID" und den zugehörigen Lösungstext.
         * 4.3. Speichern Sie diese Daten in einer Map, die FID als Schlüssel und den Lösungstext als Wert enthält.
         */
        Element loesungenElem = root.getChild("Lösungen");
        List<Element> loesungElems = loesungenElem.getChildren("Loesung");
        Map<String, String> solutionMap = new HashMap<>();

        for (Element l : loesungElems) {
            String fid = l.getAttributeValue("FID");
            String solutionText = l.getText().trim();
            solutionMap.put(fid, solutionText);
        }

        /**
         * Aufgabe 5:
         * Ordnen Sie für jede Frage die korrekte Antwort zu.
         *
         * Teilschritte:
         * 5.1. Vergleichen Sie für jedes QuestionWrapper-Objekt den Lösungstext (aus der Map)
         *      mit den vier Antworttexten.
         * 5.2. Setzen Sie die korrekte Antwort im Question-Objekt.
         * 5.3. Entscheiden Sie, wie mit Fällen umgegangen wird, in denen keine Lösung gefunden wurde.
         */
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
                    // Überlegen Sie, ob hier ein Standardwert gesetzt werden soll oder eine Exception sinnvoll ist.
                }
            } else {
                System.err.println("Keine Lösung für Frage " + qw.question.getFid());
                // Implementieren Sie hier eine einfache Fehlerbehandlung.
            }

            /**
             * Aufgabe 6:
             * Ordnen Sie die Frage der entsprechenden Kategorie zu.
             *
             * Teilschritte:
             * 6.1. Nutzen Sie dazu die Kategorie-ID (KID) aus dem QuestionWrapper.
             * 6.2. Fügen Sie das Question-Objekt der Liste der Fragen in der passenden Category hinzu.
             * 6.3. Falls die Kategorie nicht existiert, geben Sie eine Fehlermeldung aus.
             */
            Category cat = categoryMap.get(qw.kid);
            if (cat != null) {
                cat.addQuestion(qw.question);
            } else {
                System.err.println("Kategorie " + qw.kid + " nicht gefunden für Frage " + qw.question.getFid());
                // Hier können Sie überlegen, ob Sie die Kategorie dynamisch erstellen möchten.
            }
        }
        return categories;
    }

    /**
     * Aufgabe 8:
     * Erstellen Sie eine Hilfsklasse QuestionWrapper, die folgende Daten speichert:
     * - Das Question-Objekt
     * - Die zugehörige Kategorie-ID (KID)
     * - Die Originalantworttexte (AntwortA bis AntwortD)
     */
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
