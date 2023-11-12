package ru.clevetec.exception;

public class UnsupportedTimeException extends JavaTimeFormatterException {

    public UnsupportedTimeException() {
        super();
    }

    public UnsupportedTimeException(String message) {
        super(message);
    }

    public UnsupportedTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedTimeException(Throwable cause) {
        super(cause);
    }
}
