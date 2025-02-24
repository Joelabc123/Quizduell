package protocol;

public class Solution {
    private String fID;
    private String text;

    public Solution(String fID, String text) {
        this.fID = fID;
        this.text = text;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
