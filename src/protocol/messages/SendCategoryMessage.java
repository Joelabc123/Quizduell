package protocol.messages;

import java.util.ArrayList;

public class SendCategoryMessage extends Message {
    private static final long serialVersionUID = 1L;

    ArrayList<String> categories;
    public SendCategoryMessage(ArrayList<String> categories) {
        super(MessageType.SEND_CATEGORY);
        this.categories = categories;
    }

    @Override
    public MessageType getType() {
        return MessageType.SEND_CATEGORY;
    }
}
