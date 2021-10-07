package nl.abnamro.app.exception;

/**
 * Resource not found throws when a resource not exists
 *
 * @author Reza Nojavan
 */
public class ResourceNotFoundException extends GeneralException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
