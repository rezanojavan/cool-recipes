package nl.abnamro.app.exception;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle All exception
 *
 * @see ResponseEntityExceptionHandler
 */
@ControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle all unhandled exceptions and reply internal server error as response
     *
     * @param ex      the exception
     * @param request the request
     * @see GeneralException
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        logger.error("Got exception : ", ex);
        return handleExceptionInternal(ex, createResponse(
                "Internal Error", "Please repeat the request if got error again then call support"), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        logger.error("Got exception : ", ex);
        GeneralExceptionResponse response = createResponse("Forbidden", "Access denied");
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handle all {@link GeneralException} and reply with proper object as response
     *
     * @param ex      the exception {@link GeneralException}
     * @param request the request
     * @return {@link GeneralExceptionResponse}
     * @see GeneralException
     */
    @ExceptionHandler(value = {GeneralException.class})
    public ResponseEntity<Object> handleGeneralException(GeneralException ex, WebRequest request) {
        logger.error("Got exception : ", ex);
        GeneralExceptionResponse response = createResponse(ex.getMessage(), "");
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle all {@link ResourceNotFoundException} and reply with proper object as response
     *
     * @param ex      the exception {@link ResourceNotFoundException}
     * @param request the request   {@link WebRequest}
     * @return {@link GeneralExceptionResponse}
     * @see GeneralException
     */
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleAccessDeniedException(ResourceNotFoundException ex, WebRequest request) {
        logger.error("Got exception : ", ex);
        return handleExceptionInternal(ex,
                createResponse(ex.getMessage(), "Requested resource not found. Don't repeat the request"),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handle field validation exception. This method will return a list of validation error to client and says what
     * really went wrong
     *
     * @param ex      the exception
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the response
     * @see GeneralException
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got exception : ", ex);
        List<GeneralExceptionResponse> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(createResponse("Invalid Input",
                    String.format("Field %s is invalid. %s", error.getField(), error.getDefaultMessage())));
        }
        return handleExceptionInternal(ex, errors, headers,
                status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got method not supported exception : ", ex);
        GeneralExceptionResponse response = createResponse("Not supported",
                String.format("Request method %s is not supported", ex.getMethod()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got media not supported exception : ", ex);
        GeneralExceptionResponse response = createResponse("Not supported",
                String.format("Media Type %s is not supported", ex.getContentType()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got media not acceptable exception : ", ex);
        GeneralExceptionResponse response = createResponse("Not supported",
                String.format("Media Type %s is not supported", headers.getAccept()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got missing path variable exception : ", ex);
        GeneralExceptionResponse response = createResponse("Missing path variable",
                String.format("Path variable %s missing", ex.getVariableName()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got missing servlet request param exception : ", ex);
        GeneralExceptionResponse response = createResponse("Missing request parameter",
                String.format("Servlet request param %s is missing", ex.getParameterName()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got servlet request binding exception : ", ex);
        GeneralExceptionResponse response = createResponse("Internal Error", "Servlet request binding failed");
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got conversion not supported exception : ", ex);
        GeneralExceptionResponse response = createResponse("Conversion Not Supported",
                String.format("Conversion of field %s failed", ex.getPropertyName()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got type mismatch exception : ", ex);
        GeneralExceptionResponse response = createResponse("Internal Error", "type mismatch");
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got message not readable exception : ", ex);
        GeneralExceptionResponse response = createResponse("Internal Error", "message is not readable");
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got message not writable exception : ", ex);
        GeneralExceptionResponse response = createResponse("Internal Error", "message is not writable");
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got missing servlet request part exception : ", ex);
        GeneralExceptionResponse response = createResponse("Internal Error", "servlet request part is missing");
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got bind exception : ", ex);
        GeneralExceptionResponse response = createResponse("Internal Error", "binding is failed");
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Got no handler found exception : ", ex);
        GeneralExceptionResponse response = createResponse("Not found",
                String.format("No handler found for %s. Please avoid to resending", ex.getRequestURL()));
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.error("Got async request timeout exception : ", ex);
        GeneralExceptionResponse response = createResponse("Time out exception", "Async request time out");
        return this.handleExceptionInternal(ex, response, headers, status, webRequest);
    }

    /**
     * This method create general response for exception
     *
     * @param title to set when no message available
     * @param desc  to set when no message available
     * @return GeneralExceptionResponse
     */
    private GeneralExceptionResponse createResponse(String title, String desc) {
        return GeneralExceptionResponse.builder()
                .title(title)
                .description(desc)
                .build();
    }

}
