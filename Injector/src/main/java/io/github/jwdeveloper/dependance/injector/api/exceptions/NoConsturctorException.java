package io.github.jwdeveloper.dependance.injector.api.exceptions;

public class NoConsturctorException extends ContainerException
{
    public NoConsturctorException() {
    }

    public NoConsturctorException(String message) {
        super(message);
    }

    public NoConsturctorException(String message, Object... values) {
        super(message, values);
    }

    public NoConsturctorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoConsturctorException(Throwable cause) {
        super(cause);
    }

    public NoConsturctorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
