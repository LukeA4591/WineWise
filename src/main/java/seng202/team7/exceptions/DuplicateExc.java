package seng202.team7.exceptions;

/**
 * Exception to throw when data has same key as exiting item in database
 */
public class DuplicateExc  extends Exception{
    /**
     * Sends the message to superclass.
     * @param msg Exception message.
     */
    public DuplicateExc(String msg) {
        super(msg);
    }}
