package server;

public interface Game {
    GameState createGame(int size);

    GameState addPlayer(Player player);

    GameState leaveGame(Player player);

    GameState getGameState();

    void setGameState(GameState gameState);
}
