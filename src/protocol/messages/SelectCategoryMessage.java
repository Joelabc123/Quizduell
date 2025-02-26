package protocol.messages;

public class SelectCategoryMessage extends Message {
    private static final long serialVersionUID = 1L;

    String categories;
    public SelectCategoryMessage(String categories) {
        super(MessageType.SELECT_CATEGORY);
        this.categories = categories;
    }

    @Override
    public MessageType getType() {
        return MessageType.SELECT_CATEGORY;
    }
}
