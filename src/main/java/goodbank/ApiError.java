package goodbank;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private List<Error> errors;

    @Data
    @AllArgsConstructor
    static public class Error {
        private String code;
        private String title;
    }
}
