package ru.clevetec.exception;

public class JsonNullValidationException extends JsonValidationException {

    public JsonNullValidationException() {
        super();
    }

    public JsonNullValidationException(String message) {
        super(message);
    }

    public JsonNullValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonNullValidationException(Throwable cause) {
        super(cause);
    }
}
