package protocol;

public class LoginMessage extends Message {
    private final String username;

    public LoginMessage(String username) {
        super(MessageType.REGISTER);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Willkommen, " + username + "!";
    }
}