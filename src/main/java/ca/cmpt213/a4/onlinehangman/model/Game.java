/**
 * CMPT 213 Assignment 4
 * Author: Guian Gumpac
 * Email: ggumpac@sfu.ca
 * Student #: 301308462
 */

package ca.cmpt213.a4.onlinehangman.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Is the model for the hangman game
 */
public class Game {
    private int id;
    private String word; // Word to guess
    private int numGuesses;
    private int wrongGuesses;
    private String guess; // Current guess of the user
    private List<String> guesses; // Record of user guesses
    private String wordGuess; // Current progress of the word to guess
    private String status; // Game status
    private String picture; // path for hangman picture
    final private String ACTIVE = "Active";
    final private String WON = "Won";
    final private String LOST = "Lost";

    /**
     * Gets called by the controller to process the user's guess
     * @return a String the is the progress of the user
     */
    public String guess() {
        String progress = "";
        boolean newGuess = true;
        final int MAX_WRONG_GUESS = 5;

        if (guess.isEmpty()) {
            for (int i = 0; i < wordGuess.length(); i++) {
                progress += wordGuess.charAt(i) + " ";
            }
            return progress;
        }

        // We do this to avoid duplication in guesses
        for (String s : guesses) {
            if (guess.equals(s)) {
                newGuess = false;
                break;
            }
        }

        if (newGuess) {
            guesses.add(guess);
        }

        if (guess.length() > 1) {       // User guess the whole word
            progress = guessWholeWord();
        } else {       // User guesses one character
            progress = guessOneLetter();
        }

        numGuesses++;
        if (wordGuess.equals(word)) {
            status = WON;
            wordGuess = word;
            progress = word;
        } else if (wrongGuesses > MAX_WRONG_GUESS) {
            status = LOST;
        }

        guess = "";     //Clear the input box
        updatePicture();
        return progress;
    }

    /**
     * User guesses the whole word
     * @return the progress of the user
     */
    private String guessWholeWord () {
        String progress = "";

        if (guess.equals(word)) {
            status = WON;
            wordGuess = word;

            for (int i = 0; i < word.length(); i++) {
                progress += word.charAt(i) + " ";
            }
        }

        else {
            wrongGuesses++;
            for (int i = 0; i < wordGuess.length(); i++) {
                progress += wordGuess.charAt(i) + " ";
            }
        }

        return progress;
    }

    /**
     * User guesses just one letter
     * @return the progress of the user
     */
    private String guessOneLetter () {
        String currChar = guess;
        StringBuilder currGuessWord = new StringBuilder();
        String progress = "";
        boolean correctGuess = false;

        // Update the progress
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == currChar.charAt(0)) {
                currGuessWord.append(currChar);
                correctGuess = true;
            } else if (wordGuess.charAt(i) != '_') {
                currGuessWord.append(wordGuess.charAt(i));
            } else {
                currGuessWord.append("_");
            }
            progress += currGuessWord.charAt(i) + " ";
        }

        if (!correctGuess) {
            wrongGuesses++;
        }
        wordGuess = currGuessWord.toString();

        return progress;
    }

    /**
     * Update the hangman picture based on the number of incorrect guesses
     */
    private void updatePicture() {
        picture = "/images/";

        switch (wrongGuesses) {
            case 0:
                picture += "Empty.jpg";
                break;
            case 1:
                picture += "Head.jpg";
                break;
            case 2:
                picture += "Torso.jpg";
                break;
            case 3:
                picture += "LeftLeg.jpg";
                break;
            case 4:
                picture += "RightLeg.jpg";
                break;
            case 5:
                picture += "RightArm.jpg";
                break;
            case 6:
                picture += "LeftArm.jpg";
                break;
        }
    }

    /**
     * Getter for the user's current progress
     * We do this to create spacing for each character
     * @return the progress of guessing the word
     */
    public String getProgress() {
        String progress = "";

        progress += wordGuess.charAt(0);
        for (int i = 1; i < wordGuess.length(); i++) {
            progress += " " + wordGuess.charAt(i);
        }

        return progress;
    }

    /**
     * Initializes the guesses field
     */
    public void initGuesses() {
        this.guesses = new ArrayList<>();
    }

    /**
     * Initializes the path of the hangman picture
     */
    public void initPicture() {
        this.picture = "/images/Empty.jpg";
    }

    // Getters and setters

    /**
     * Getter for the word to be guessed
     * @return the word to be guessed
     */
    public String getWord() {
        return word;
    }

    /**
     * Getter for the ID field
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the list of user guesses
     * @return list of all previous guesses
     */
    public String getGuess() {
        return guess;
    }

    /**
     * Getter for the number of user guesses
     * @return the number of guesses
     */
    public int getNumGuesses() {
        return numGuesses;
    }

    /**
     * Getter for the status of the game
     * @return ACTIVE, WON, or LOST
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for the number of wrong guesses
     * @return the number of wrong guesses
     */
    public int getWrongGuesses() {
        return wrongGuesses;
    }

    /**
     * Getter for the list of guesses
     * @return the list of guesses
     */
    public List<String> getGuesses() {
        return guesses;
    }

    /**
     * Getter for the picture for hangman
     * @return the path to the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Setter for the id
     * @param id is the id of the game
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for the word to be guessed
     * @param word is the word to be guessed
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Setter for the number of guesses
     * @param numGuesses is the number of guesses
     */
    public void setNumGuesses(int numGuesses) {
        this.numGuesses = numGuesses;
    }

    /**
     * Sets the game status to active
     */
    public void setStatusActive() {
        this.status = ACTIVE;
    }

    /**
     * Setter for the number of wrong guesses
     * @param wrongGuesses is the number of wrong guesses
     */
    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }

    /**
     * Setter for the current guess
     * @param guess is the user's most recent guess
     */
    public void setGuess(String guess) {
        this.guess = guess;
    }

    /**
     * Setter for the progress of the user
     * @param wordGuess is the progress with guessing the word
     */
    public void setWordGuess(String wordGuess) {
        this.wordGuess = wordGuess;
    }
}