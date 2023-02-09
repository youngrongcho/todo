package soloProject.soloProject.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LogicExceptionEnum {
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할 일 목록이 없습니다.");

    final HttpStatus httpStatus;
    final String message;

    LogicExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
