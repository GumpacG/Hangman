/**
 * CMPT 213 Assignment 4
 * Author: Guian Gumpac
 * Email: ggumpac@sfu.ca
 * Student #: 301308462
 */

package ca.cmpt213.a4.onlinehangman.controllers;

/**
 * Custom exception for when a game is not found games list
 */
public class GameNotFoundException extends RuntimeException{
    /**
     * Default constructor
     * @param message is the error message
     */
    public GameNotFoundException(String message) {
        super(message);
    }
}
