package protocol.messages;

import protocol.Category;

public class SelectCategoryMessage extends Message {
    private static final long serialVersionUID = 1L;

    Category category;
    public SelectCategoryMessage(Category category) {
        super(MessageType.SELECT_CATEGORY);
        this.category = category;
    }

    @Override
    public MessageType getType() {
        return MessageType.SELECT_CATEGORY;
    }

    public Category getCategory() {
        return category;
    }
}
