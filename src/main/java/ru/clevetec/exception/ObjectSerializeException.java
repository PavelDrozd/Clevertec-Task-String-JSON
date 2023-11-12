package ru.clevetec.exception;

public class ObjectSerializeException extends StringJsonMapperException {

    public ObjectSerializeException() {
        super();
    }

    public ObjectSerializeException(String message) {
        super(message);
    }

    public ObjectSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectSerializeException(Throwable cause) {
        super(cause);
    }
}
