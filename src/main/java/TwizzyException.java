/**
 * Represents an invalid command or command argument supplied to Twizzy.
 */
public class TwizzyException extends Exception {
    /**
     * Creates an exception with a user-friendly explanation.
     *
     * @param message explanation of the input problem and how to correct it
     */
    public TwizzyException(String message) {
        super(message);
    }
}
