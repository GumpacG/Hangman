/**
 * CMPT 213 Assignment 4
 * Author: Guian Gumpac
 * Email: ggumpac@sfu.ca
 * Student #: 301308462
 */

package ca.cmpt213.a4.onlinehangman.controllers;

import ca.cmpt213.a4.onlinehangman.model.Game;
import ca.cmpt213.a4.onlinehangman.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The controller for the Hangman game
 */
@Controller
@SessionAttributes("game")
public class HangmanController {
    private Message promptMessage; //a reusable String object to display a prompt message at the screen
    private List<Game> games = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong();
    private List<String> words = new ArrayList<>(); // List of words to choose from

    /**
     * Constructor for class
     */
    @PostConstruct
    public void hangmanControllerInit() {
        promptMessage = new Message("Initializing...");

        // Load the list of words
        try {
            File file = new File("commonWords.txt");
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            String line = "";

            while (line != null) {
                try {
                    line = br.readLine();
                    words.add(line);
                }
                catch (IOException e) {
                    // Do nothing
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Words file not found!");
        }
    }

    /**
     * GET request method to get to home.html
     * @param model is the model of the game
     * @return the file name of the home.html file
     */
    @GetMapping("/home")
    public String showHomePage(Model model) {
        promptMessage.setMessage("The Hangman");
        model.addAttribute("promptMessage", promptMessage);

        // take the user to home.html
        return "home";
    }

    /**
     * GET request method to get to game.html
     * @param model is the model of the game
     * @return the file name of the game.html file
     */
    @GetMapping("/game")
    public String playGame(Model model) {
        Game game = new Game();
        String randomWord = getRandomWord();
        String wordGuess = "";
        String progress = "";
        String numGuessMessage = "";

        // initializes the progress to be displayed
        for (int i = 0; i < randomWord.length(); i++) {
            wordGuess = wordGuess.concat("_");
            progress += "_ ";
        }

        // Constructs the Game object
        game.setWordGuess(wordGuess);
        game.setId((int) nextId.incrementAndGet());
        game.setWord(randomWord);
        game.setStatusActive();
        game.setNumGuesses(0);
        game.initGuesses();
        game.initPicture();

        numGuessMessage = "You have made " + game.getNumGuesses() + " guesses, out of which "
                + game.getWrongGuesses() + " are incorrect.";

        promptMessage.setMessage("Game " + game.getId());
        model.addAttribute("promptMessage", promptMessage);
        model.addAttribute("game", game);
        model.addAttribute("status", game.getStatus());
        model.addAttribute("numGuessMessage", numGuessMessage);
        model.addAttribute("wrongGuess", game.getWrongGuesses());
        model.addAttribute("progress", progress);
        model.addAttribute("picture", game.getPicture());
        games.add(game);

        // take the user to game.html
        return "game";
    }

    /**
     * GET request method to update information and get to game.html
     * @param model is the model of the game
     * @param game is the Game object from game.html
     * @return the file name of the game.html file
     */
    @PostMapping("/game")
    public String guessLetter(Model model, @ModelAttribute("game") Game game) {
        String numGuessMessage;
        String progress = game.guess();
        String guesses = game.getGuesses().get(0);

        for (int i = 1; i < game.getGuesses().size(); i++) {
            guesses += ", " + game.getGuesses().get(i);
        }

        numGuessMessage = "You have made " + game.getNumGuesses() + " guesses, out of which "
                + game.getWrongGuesses() + " are incorrect.";

        promptMessage.setMessage("Game " + game.getId());
        model.addAttribute("promptMessage", promptMessage);
        model.addAttribute("status", game.getStatus());
        model.addAttribute("numGuessMessage", numGuessMessage);
        model.addAttribute("progress", progress);
        model.addAttribute("guesses", guesses);
        model.addAttribute("picture", game.getPicture());

        if (game.getStatus().equals("Won") || game.getStatus().equals("Lost")) {
            model.addAttribute("progress", game.getWord());
            return "gameover";
        }

        // take the user to game.html
        return "game";
    }

    /**
     * GET request method to get to game.html with the same id
     * @param gameID is the id of the game we are looking for
     * @param model is the model of the game
     * @return the file name of the game.html file
     */
    @GetMapping("/game/{id}")
    public String getGame(@PathVariable("id") long gameID, Model model) {
        Game foundGame = null;
        String guesses = "";
        String numGuessMessage = "";

        for (Game game : games) {
            if (game.getId() == gameID) {
                foundGame = game;
            }
        }

        if (foundGame == null) {
            throw new GameNotFoundException("Request ID not found.");
        } else {
            numGuessMessage = "You have made " + foundGame.getNumGuesses() + " guesses, out of which "
                    + foundGame.getWrongGuesses() + " are incorrect.";

            if (!foundGame.getGuesses().isEmpty()) {
                guesses = foundGame.getGuesses().get(0);
                for (int i = 1; i < foundGame.getGuesses().size(); i++) {
                    guesses += ", " + foundGame.getGuesses().get(i);
                }
            }

            promptMessage.setMessage("Game " + foundGame.getId());
            model.addAttribute("promptMessage", promptMessage);
            model.addAttribute("game", foundGame);
            model.addAttribute("status", foundGame.getStatus());
            model.addAttribute("numGuessMessage", numGuessMessage);
            model.addAttribute("wrongGuess", foundGame.getWrongGuesses());
            model.addAttribute("progress", foundGame.getProgress());
            model.addAttribute("guesses", guesses);
            model.addAttribute("picture", foundGame.getPicture());
        }

        if (foundGame.getStatus().equals("Won") || foundGame.getStatus().equals("Lost")) {
            model.addAttribute("progress", foundGame.getWord());
            return "gameover";
        }

        // take the user to game.html
        return "game";
    }

    /**
     * Exception handler for GameNotFoundException
     * @return the file name of the gamenotfound.html file
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(GameNotFoundException.class)
    public String gameNotFoundExceptionHandler() {
        return "gamenotfound";
    }

    /**
     * This gives the Game class a random word that the user will guess
     * @return the random word
     */
    private String getRandomWord() {
        Random random = new Random();
        int randomElement = random.nextInt(words.size());
        return words.get(randomElement);
    }
}