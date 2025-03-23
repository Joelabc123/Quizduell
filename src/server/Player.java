package server;

import protocol.Category;
import protocol.CategoryRound;
import protocol.Question;
import protocol.messages.*;
import utils.Usernames;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
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

    public UUID getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Beim Verbindungsaufbau sendet der Server eine LoginMessage
            sendMessage(new LoginMessage(id, username));

            while (socket.isConnected()) {
                Object received = in.readObject();
                System.out.println("Received: " + received.getClass().getSimpleName());
                Message message = (Message) received;

                switch (message.getType()) {
                    case MessageType.HOST_LOBBY:
                        System.out.println("Received HostLobbyMessage");
                        HostLobbyMessage hostLobbyMessage = (HostLobbyMessage) received;
                        this.ingame = true;
                        // Neues QuizGame anhand der aktuellen Kategorien des globalen QuizReaders erzeugen
                        QuizGame game = new QuizGame(new ArrayList<>(Server.quizReader.categories));
                        Thread gameThread = new Thread(game);
                        game.addPlayer(this);
                        server.registerGame(game);
                        break;
                    case MessageType.SEND_QUIZSET:
                        // NEU: Quizset aus der Nachricht verarbeiten
                        String quizset = ((SendQuizsetMessage) received).getQuizset();
                        System.out.println("Received SEND_QUIZSET: " + quizset);
                        File quizFile = new File("resources", quizset);
                        if (quizFile.exists() && quizFile.isFile()) {
                            // Erzeuge einen neuen QuizReader mit der ausgewählten Datei
                            QuizReader newQuizReader = new QuizReader(quizFile.getPath());
                            // Aktualisiere den globalen QuizReader
                            Server.quizReader = newQuizReader;
                            System.out.println("Quizset updated to: " + quizset);
                        } else {
                            System.out.println("Quizset file not found: " + quizFile.getAbsolutePath());
                        }
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
                        System.out.println("Received SelectCategoryMessage: " + selectCategoryMessage.getCategory().getName());
                        Category selectedCategory = selectCategoryMessage.getCategory();
                        QuizGame quizGame = server.getGame(this);
                        for (Category c : quizGame.getAvailableCategories()) {
                            if (c.getKatID().equals(selectedCategory.getKatID())) {
                                System.out.println("Kategorie gefunden: " + c.getName());
                                CategoryRound categoryRound = new CategoryRound(c);
                                fillCategoryRound(categoryRound);
                                quizGame.getGameState().addRound(categoryRound);
                                break;
                            }
                        }
                        // Sende aktualisierte Kategorien an alle Spieler
                        GameState gameState = new GameState(quizGame.getGameState());
                        SendCategoriesMessage scm = new SendCategoriesMessage(gameState);
                        quizGame.broadcast(scm);
                        quizGame.getAvailableCategories().remove(selectedCategory);
                        break;
                    case MessageType.ANSWER_QUESTION:
                        AnswerQuestionMessage answerQuestionMessage = (AnswerQuestionMessage) received;
                        ArrayList<Boolean> answerList = answerQuestionMessage.getAnswers();
                        System.out.println("Received AnswerQuestionMessage: " + answerList);
                        QuizGame answerGame = server.getGame(this);
                        GameState gs = answerGame.getGameState();
                        CategoryRound currentRound = gs.getCurrentRound();

                        if (this.getId().equals(answerGame.getPlayerA().getId())) {
                            currentRound.setAnswersPlayerA(new ArrayList<>(answerList));
                            System.out.println("Antwort von Player A erhalten: " + answerList);
                        } else if (this.getId().equals(answerGame.getPlayerB().getId())) {
                            currentRound.setAnswersPlayerB(new ArrayList<>(answerList));
                            System.out.println("Antwort von Player B erhalten: " + answerList);
                        } else {
                            System.out.println("Antwort von unbekanntem Spieler: " + this.getId());
                        }

                        if (currentRound.getAnswersPlayerA().size() == 3 && currentRound.getAnswersPlayerB().size() == 3) {
                            gs.switchPlayerTurn();
                            gs.incrementCurrentRound();
                            currentRound.setWinner();
                            System.out.println("Winner: " + currentRound.getWinner());
                            gs.updateScores();
                            GameState gameState2 = new GameState(gs); // Tiefe Kopie
                            answerGame.broadcast(new UpdateGameMessage(gameState2));
                            if (gs.isLastRound()) {
                                System.out.println("Game Over");
                                answerGame.broadcast(new GameOverMessage(gameState2));
                            }
                            answerGame.broadcast(new PlayerTurnMessage(gameState2));
                        }
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Verbindung mit " + username + " verloren.");
            server.removePlayer(this);
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

    public ObjectInputStream getIn() {
        return in;
    }

    public void fillCategoryRound(CategoryRound cr) {
        cr.initializeQuestions();
        Category category = cr.getCategory();
        ArrayList<Question> availableQuestions = category.getQuestions();
        if (availableQuestions.size() < 3) {
            throw new IllegalStateException("Nicht genügend Fragen vorhanden, um eine Runde zu füllen.");
        }
        Collections.shuffle(availableQuestions);
        for (int i = 0; i < 3; i++) {
            Question q = availableQuestions.remove(0);
            cr.addQuestion(q);
        }
    }
}
