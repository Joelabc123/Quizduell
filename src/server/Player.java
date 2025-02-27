package server;

import protocol.Category;
import protocol.CategoryRound;
import protocol.Question;
import protocol.QuestionRound;
import protocol.messages.*;
import utils.Usernames;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Player implements Runnable {

    private Server server;

    private Socket socket;
    private UUID id;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    private String ip;

    public Player(Socket socket, Server server) {
        this.socket = socket;
        this.id = UUID.randomUUID();
        this.ip = socket.getInetAddress().getHostAddress();
        this.username = Usernames.generate();
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            //When client connects to server
            sendMessage(new LoginMessage(id, username));

            while (socket.isConnected()) {
                Object received = in.readObject();

                System.out.println("Received: " + received.getClass().getSimpleName());

                Message message = (Message) received;

                switch (message.getType()) {
                    case MessageType.HOST_LOBBY:
                        HostLobbyMessage hostLobbyMessage = (HostLobbyMessage) received;

                        //Register new QuizGame
                        QuizGame game = new QuizGame();
                        Thread gameThread = new Thread(game);

                        game.addPlayer(this);
                        server.registerGame(game, gameThread);

                        break;
                    case MessageType.JOIN_LOBBY:
                        JoinLobbyMessage joinLobbyMessage = (JoinLobbyMessage) received;

                        QuizGame targetGame = null;

                        for (QuizGame quizGame : server.getGames()) {
                            if (quizGame.getGameState().getLobbyCode() == joinLobbyMessage.getLobbyCode()) {
                                targetGame = quizGame;
                            }
                        }

                        if (targetGame == null) {
                            sendMessage(new ErrorMessage(ErrorType.AUTHENTICATION_FAILED));
                            break;
                        }

                        targetGame.addPlayer(this);

                        break;
                    case SELECT_CATEGORY:
                        SelectCategoryMessage selectCategoryMessage = (SelectCategoryMessage) received;

                        Category selectedCategory = selectCategoryMessage.getCategory();

                        QuizGame quizGame = server.getGameFromPlayer(this);
                        for (Category c : Server.quizReader.categories) {
                            if (c.getKatID().equals(selectedCategory.getKatID())) {
                                CategoryRound categoryRound = new CategoryRound(c);
                                quizGame.getGameState().addRound(categoryRound);
                            }
                        }

                        sendMessage(new SendQuestionMessage(quizGame.getGameState()));

                        break;
                    case ANSWER_QUESTION:
                        AnswerQuestionMessage answerQuestionMessage = (AnswerQuestionMessage) received;
                        Answer selectedAnswer = answerQuestionMessage.getSelectedAnswer();

                        QuizGame answerGame = server.getGameFromPlayer(this);
                        CategoryRound currentCategoryRound = answerGame.getGameState().getCurrentRound();

                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Verbindung mit " + username + " verloren .");
            //server.removePlayer(this);
            //server.removeFromQueue(this.getId());
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }
}