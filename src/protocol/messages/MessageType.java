package protocol.messages;

public enum MessageType {
    // Server to Client
    LOGIN,
    SEND_QUESTION,
    UPDATE_GAME,
    ERROR,

    // Client to Server
    JOIN_LOBBY,
    HOST_LOBBY,
    SELECT_CATEGORY,
    ANSWER_QUESTION
}

