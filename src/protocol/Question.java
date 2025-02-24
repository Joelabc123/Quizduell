package protocol;

public class Question {
    private String fid;
    private String frageName;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private Solution correctAnswer;
    private boolean isCorrect;

    public Question(String fid, String frageName, String answerA, String answerB, String answerC, String answerD, Solution correctAnswer) {
        this.fid = fid;
        this.frageName = frageName;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public Question(String fid, String kid, String frageName, String answerA, String answerB, String answerC, String answerD) {
        this.fid = fid;
        this.kid = kid;
        this.frageName = frageName;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
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

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
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
