package ru.clevetec.exception;

public class JsonDeserializeException extends StringJsonMapperException {

    public JsonDeserializeException() {
        super();
    }

    public JsonDeserializeException(String message) {
        super(message);
    }

    public JsonDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonDeserializeException(Throwable cause) {
        super(cause);
    }
}
