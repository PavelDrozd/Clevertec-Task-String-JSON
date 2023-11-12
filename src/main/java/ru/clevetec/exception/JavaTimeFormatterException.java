package ru.clevetec.exception;

public class JavaTimeFormatterException extends StringJsonMapperException {

    public JavaTimeFormatterException() {
        super();
    }

    public JavaTimeFormatterException(String message) {
        super(message);
    }

    public JavaTimeFormatterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavaTimeFormatterException(Throwable cause) {
        super(cause);
    }
}
