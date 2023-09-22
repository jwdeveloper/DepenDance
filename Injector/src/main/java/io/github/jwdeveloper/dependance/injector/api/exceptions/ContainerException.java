package io.github.jwdeveloper.dependance.injector.api.exceptions;

public class ContainerException extends RuntimeException
{
    public ContainerException() {
    }

    public ContainerException(String message) {
        super(message);
    }

    public ContainerException(String message, Object... values) {
        super(String.format(message,values));
    }


    public ContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerException(Throwable cause) {
        super(cause);
    }

    public ContainerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
