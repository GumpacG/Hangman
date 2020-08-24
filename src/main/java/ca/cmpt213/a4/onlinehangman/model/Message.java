/**
 * CMPT 213 Assignment 4
 * Author: Guian Gumpac
 * Email: ggumpac@sfu.ca
 * Student #: 301308462
 */

package ca.cmpt213.a4.onlinehangman.model;

/**
 * Is the message being passed to the html pages
 */
public class Message {
    private String message;

    /**
     * Constructor
     */
    public Message() {
        this.message = "";
    }

    /**
     * Constructor
     * @param s is the message to be passed to html pages
     */
    public Message(String s) {
        this.message = s;
    }

    /**
     * Getter for the message
     * @return the message field
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message field
     * @param message is the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
