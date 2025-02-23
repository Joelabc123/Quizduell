package protocol.messages;

public enum MessageType {
    //Server to Client
    LOGIN,
    //Client to Server
    JOIN_LOBBY,
    //Client to Server
    HOST_LOBBY,
    //Server to Client
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
