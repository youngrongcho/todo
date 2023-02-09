package soloProject.soloProject.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorResponse {
    private String message;
    private HttpStatus httpStatus;
    private List<FieldError> fieldErrorList;
    private List<Constraint> constraints;

    public ErrorResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ErrorResponse(List<FieldError> fieldErrorList, List<Constraint> constraints) {
        this.fieldErrorList = fieldErrorList;
        this.constraints = constraints;
    }

    public static ErrorResponse of(HttpRequestMethodNotSupportedException e){
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> constraintViolations){
        return new ErrorResponse(null, Constraint.of(constraintViolations));
    }

    public static ErrorResponse of(BindingResult bindingResult){
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }


    @Getter
    private static class Constraint {
        private String message;
        private String propertyPath;
        private String invalidValue;

        private Constraint(String message, String propertyPath, String invalidValue) {
            this.message = message;
            this.propertyPath = propertyPath;
            this.invalidValue = invalidValue;
        }

        private static List<Constraint> of(Set<ConstraintViolation<?>> constraintViolations){
            return constraintViolations.stream().map(
                    constraintViolation -> new Constraint(constraintViolation.getMessage(),
                                constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getInvalidValue().toString())
            ).collect(Collectors.toList());
        }
    }

    private static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;

        private FieldError(String field, String rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        private static List<FieldError> of(BindingResult bindingResult){
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream().map(
                    fieldError -> new FieldError(fieldError.getField(),
                            fieldError.getRejectedValue().toString(), fieldError.getDefaultMessage())
            ).collect(Collectors.toList());
        }
    }
}
