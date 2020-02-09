package goodbank;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Wrapper for error response. Structure is inspired by JsonAPI.
 */
@Data
@AllArgsConstructor
public class ApiError {
    private List<Error> errors;

    @Data
    @AllArgsConstructor
    static public class Error {
        private String status;
        private String title;
    }
}
