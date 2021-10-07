package nl.abnamro.app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This class delivers general exception information
 *
 * @author Reza Nojavan
 */
@Data
@AllArgsConstructor
@Builder
public class GeneralExceptionResponse {

    /**
     * Exception title
     */
    private String title;

    /**
     * Exception description
     */
    private String description;

}
