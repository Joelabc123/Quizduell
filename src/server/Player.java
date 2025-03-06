package server;

import protocol.Category;
import protocol.CategoryRound;
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

    private boolean ingame = false;

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
                        this.ingame = true;
                        //Register new QuizGame
                        QuizGame game = new QuizGame();
                        Thread gameThread = new Thread(game);

                        game.addPlayer(this);
                        server.registerGame(game);

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
                    case MessageType.SELECT_CATEGORY:
                        SelectCategoryMessage selectCategoryMessage = (SelectCategoryMessage) received;

                        Category selectedCategory = selectCategoryMessage.getCategory();

                        QuizGame quizGame = server.getGame(this);
                        for (Category c : Server.quizReader.categories) {
                            if (c.getKatID().equals(selectedCategory.getKatID())) {
                                CategoryRound categoryRound = new CategoryRound(c);
                                quizGame.getGameState().addRound(categoryRound);
                            }
                        }

                        sendMessage(new UpdateGameMessage(quizGame.getGameState()));

                        break;
                    case MessageType.ANSWER_QUESTION:

                        /// /SKIBIDIIIIIIII
                        AnswerQuestionMessage answerQuestionMessage = (AnswerQuestionMessage) received;
                        Answer selectedAnswer = answerQuestionMessage.getSelectedAnswer();
                        String fId = answerQuestionMessage.getfId();

                        QuizGame answerGame = server.getGame(this);
                        CategoryRound currentCategoryRound = answerGame.getGameState().getCurrentRound();
                        QuestionRound currentQuestionRound = currentCategoryRound.getQuestionRounds().getLast();

                        if(this.getId().equals(answerGame.getPlayerA().getId())) {
                            currentQuestionRound.setAnswerPlayerA(fId,selectedAnswer);
                        }
                        else {
                            currentQuestionRound.setAnswerPlayerB(fId,selectedAnswer);
                        }

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
    System.out.println("Sending: " + message.getClass().getSimpleName());

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

    public ObjectInputStream getIn() {
        return in;
    }

    public void setingame(boolean ingame) {
        this.ingame = ingame;
    }

    public boolean getIngame() {
        return ingame;
    }
}