package protocol.messages;

public enum MessageType {
    //Server to Client check
    LOGIN,
    //Client to Server check
    JOIN_LOBBY,
    //Client to Server check
    HOST_LOBBY,
    //Server to Client check
    LOBBY_STATUS,
    //Server to Client
    START_GAME,
    //Server to Client
    QUESTION,
    //Client to Server
    ANSWER,
    //Server to Client
    SCORE_UPDATE,
    //Server to Client
    ROUND_RESULT,
    //Server to Client
    GAME_OVER,
    //Server to Client
    ERROR
}
