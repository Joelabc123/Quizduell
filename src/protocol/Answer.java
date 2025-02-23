package protocol;

public class Answer {
    private char option; // 'A', 'B', 'C', 'D'
    private String text;

    public Answer(char option, String text) {
        this.option = option;
        this.text = text;
    }

    public char getOption() {
        return option;
    }

    public void setOption(char option) {
        this.option = option;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return option + ": " + text;
    }
}
