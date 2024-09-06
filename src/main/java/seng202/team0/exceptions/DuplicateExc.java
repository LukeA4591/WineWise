package seng202.team0.exceptions;

/**
 * Exception to throw when data has same key as exiting item in database
 */
public class DuplicateExc  extends Exception{
    /**
     * just sends the message to super
     * @param msg
     */
    public DuplicateExc(String msg) {
        super(msg);
    }
}
