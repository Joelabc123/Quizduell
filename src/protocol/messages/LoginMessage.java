package protocol.messages;

import utils.Usernames;

public class LoginMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String username;

    public LoginMessage() {
        this.username = Usernames.generate();
    }

    public String getUsername() {
        return username;
    }


    @Override
    public MessageType getType() {
        return MessageType.LOGIN;
    }
}
