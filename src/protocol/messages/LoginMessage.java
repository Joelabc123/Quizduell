package protocol.messages;

import utils.Usernames;

import java.util.UUID;

public class LoginMessage extends Message {
    private static final long serialVersionUID = 1L;

    private String username;
    private UUID userId, secret;

    public LoginMessage(UUID userId, String username) {
        super(MessageType.LOGIN);
        this.userId = userId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getSecret() {
        return secret;
    }

    @Override
    public MessageType getType() {
        return MessageType.LOGIN;
    }
}
