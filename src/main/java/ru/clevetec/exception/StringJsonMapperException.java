package ru.clevetec.exception;

public class StringJsonMapperException extends RuntimeException{

    public StringJsonMapperException() {
        super();
    }

    public StringJsonMapperException(String message) {
        super(message);
    }

    public StringJsonMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringJsonMapperException(Throwable cause) {
        super(cause);
    }
}
