package ru.clevetec.exception;

public class JsonValidationException extends StringJsonMapperException {

    public JsonValidationException() {
        super();
    }

    public JsonValidationException(String message) {
        super(message);
    }

    public JsonValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonValidationException(Throwable cause) {
        super(cause);
    }
}
