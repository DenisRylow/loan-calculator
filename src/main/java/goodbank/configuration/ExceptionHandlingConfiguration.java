package goodbank.configuration;

import goodbank.ApiError;
import goodbank.ErrorCodes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlingConfiguration {

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleValidationException(ConstraintViolationException ex, WebRequest request) {
        List<ApiError.Error> errors = new ArrayList<>();
        for (ConstraintViolation exception : ex.getConstraintViolations()) {
            ApiError.Error error = new ApiError.Error(
                    ErrorCodes.INPUT_DATA_VALIDATION_ERROR.toString(),
                    exception.getMessage()
            );
            errors.add(error);
        }
        ApiError apiError = new ApiError(errors);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.badRequest().headers(headers).body(apiError);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleCommonException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(Arrays.asList(new ApiError.Error(
                ErrorCodes.INTERNAL_SERVER_ERROR.toString(),
                "Internal Error")
        ));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(apiError);
    }
}
