package protocol;

public class Question {
    private String fid;
    private String kid; // Referenz zur Kategorie
    private String frageName;
    private Answer answerA;
    private Answer answerB;
    private Answer answerC;
    private Answer answerD;
    private char correctAnswer; // 'A', 'B', 'C' oder 'D'

    public Question(String fid, String kid, String frageName, Answer answerA, Answer answerB, Answer answerC, Answer answerD) {
        this.fid = fid;
        this.kid = kid;
        this.frageName = frageName;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = ' '; // zunächst unset
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getFrageName() {
        return frageName;
    }

    public void setFrageName(String frageName) {
        this.frageName = frageName;
    }

    public Answer getAnswerA() {
        return answerA;
    }

    public void setAnswerA(Answer answerA) {
        this.answerA = answerA;
    }

    public Answer getAnswerB() {
        return answerB;
    }

    public void setAnswerB(Answer answerB) {
        this.answerB = answerB;
    }

    public Answer getAnswerC() {
        return answerC;
    }

    public void setAnswerC(Answer answerC) {
        this.answerC = answerC;
    }

    public Answer getAnswerD() {
        return answerD;
    }

    public void setAnswerD(Answer answerD) {
        this.answerD = answerD;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "fid='" + fid + '\'' +
                ", kid='" + kid + '\'' +
                ", frageName='" + frageName + '\'' +
                ", answerA=" + answerA +
                ", answerB=" + answerB +
                ", answerC=" + answerC +
                ", answerD=" + answerD +
                ", correctAnswer=" + correctAnswer +
                '}';
    }
}
