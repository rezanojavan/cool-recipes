package nl.abnamro.app.exception;

/**
 * General Exception throws when no specific exception exists
 *
 * @author Reza Nojavan
 */
public class GeneralException extends RuntimeException {

    public GeneralException(String message) {
        super(message);
    }
}
