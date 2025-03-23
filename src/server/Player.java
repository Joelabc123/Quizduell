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
                        System.out.println("Received HostLobbyMessage");
                        HostLobbyMessage hostLobbyMessage = (HostLobbyMessage) received;
                        this.ingame = true;
                        //Register new QuizGame
                        QuizGame game = new QuizGame(new ArrayList<>(Server.quizReader.categories));
                        Thread gameThread = new Thread(game);

                        game.addPlayer(this);
                        server.registerGame(game);
                        break;
                    case MessageType.SEND_QUIZSET:
                        String quizset = ((SendQuizsetMessage) received).getQuizset();

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
                        // Finde die richtige Kategorie und füge eine neue Runde hinzu
                        for (Category c : quizGame.getAvailableCategories()) {
                            if (c.getKatID().equals(selectedCategory.getKatID())) {
                                System.out.println("kategorie gefunden" + c.getName());
                                System.out.println("c" + c.getQuestions().getFirst().getFrageName());
                                CategoryRound categoryRound = new CategoryRound(c);
                                fillCategoryRound(categoryRound);
                                quizGame.getGameState().addRound(categoryRound);
                                break;
                            }
                        }

                        // Erzeuge eine SendCategoriesMessage, die den aktuellen GameState enthält,
                        // und broadcasten Sie diese Nachricht an alle Spieler des Spiels.
                        System.out.println("CategoryRoundCheck: " + quizGame.getGameState().getCurrentRound().getCategory().getName());
                        System.out.println("CategoryRoundCheck: " + quizGame.getGameState().getCurrentRound().getCategory());
                        System.out.println("CategoryRoundCheck: " + quizGame.getGameState().getCurrentRound());
                        System.out.println("CategoryRoundCheck: " + quizGame.getGameState());

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


                        // Ermitteln, von welchem Spieler die Antwort kommt, und kopieren die Antwortliste
                        if (this.getId().equals(answerGame.getPlayerA().getId())) {
                            currentRound.setAnswersPlayerA(new ArrayList<>(answerList));
                            System.out.println("Antwort von Player A erhalten: " + answerList);
                        } else if (this.getId().equals(answerGame.getPlayerB().getId())) {
                            currentRound.setAnswersPlayerB(new ArrayList<>(answerList));
                            System.out.println("Antwort von Player B erhalten: " + answerList);
                        } else {
                            System.out.println("Antwort von unbekanntem Spieler: " + this.getId());
                        }

                        // Gewinnerberechnung und Scoreaktualisierung nur, wenn beide Spieler ihre Antworten (z.B. 3 Antworten) abgegeben haben:
                        if (currentRound.getAnswersPlayerA().size() == 3 && currentRound.getAnswersPlayerB().size() == 3) {
                            gs.switchPlayerTurn();
                            gs.incrementCurrentRound();
                            currentRound.setWinner();
                            System.out.println("Winner213213213213: " + currentRound.getWinner());
                            gs.updateScores();
                            GameState gameState2 = new GameState(gs); // Tiefe Kopie
                            System.out.println("Winner: " + currentRound.getWinner());
                            System.out.println("Score Player A: " + gs.getScorePlayerA() + ", Score Player B: " + gs.getScorePlayerB());
                            // Sende Update-Nachricht an alle Spieler:
                            answerGame.broadcast(new UpdateGameMessage(gameState2));
                            // Falls erforderlich, zusätzlich:
                            System.out.println("turnPlayerB: " + gameState2.getTurnPlayerB());
                            System.out.println("turnPlayerB: " + gameState2.getTurnPlayerB());
                            if(gs.isLastRound()){
                                System.out.println("Game Over");
                                answerGame.broadcast(new GameOverMessage(gameState2));
                            }
                            server.getGame(this).broadcast(new PlayerTurnMessage(gameState2));
                        }
                        break;


                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Verbindung mit " + username + " verloren .");
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

    public UUID getId() {
        return id;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void fillCategoryRound(CategoryRound cr) {
        // Stelle sicher, dass die Fragenliste in der CategoryRound initialisiert ist.
        cr.initializeQuestions();

        // Hole die Kategorie aus der übergebenen Runde
        Category category = cr.getCategory();
        ArrayList<Question> availableQuestions = category.getQuestions();
        if (availableQuestions.size() < 3) {
            throw new IllegalStateException("Nicht genügend Fragen vorhanden, um eine Runde zu füllen.");
        }

        // Mische die Liste einmal
        Collections.shuffle(availableQuestions);

        // Entferne die ersten drei Fragen und füge sie der CategoryRound hinzu
        for (int i = 0; i < 3; i++) {
            Question q = availableQuestions.remove(0);
            cr.addQuestion(q);
        }
    }


}