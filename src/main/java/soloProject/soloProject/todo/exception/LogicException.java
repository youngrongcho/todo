package soloProject.soloProject.todo.exception;

import lombok.Getter;

@Getter
public class LogicException extends RuntimeException{
    private LogicExceptionEnum exceptionEnum;

    public LogicException(LogicExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
