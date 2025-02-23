package protocol.messages;

import java.util.List;

public class QuestionMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String questionId;   // FID
    private String categoryId;   // KID
    private String questionText;
    private List<String> answers; // Vier Antwortmöglichkeiten

    public QuestionMessage(String questionId, String categoryId, String questionText, List<String> answers) {
        this.questionId = questionId;
        this.categoryId = categoryId;
        this.questionText = questionText;
        this.answers = answers;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public MessageType getType() {
        return MessageType.QUESTION;
    }
}
