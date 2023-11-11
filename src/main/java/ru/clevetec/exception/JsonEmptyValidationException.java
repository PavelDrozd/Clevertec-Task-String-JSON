package ru.clevetec.exception;

public class JsonEmptyValidationException extends JsonValidationException {

    public JsonEmptyValidationException() {
        super();
    }

    public JsonEmptyValidationException(String message) {
        super(message);
    }

    public JsonEmptyValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonEmptyValidationException(Throwable cause) {
        super(cause);
    }
}
