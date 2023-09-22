package io.github.jwdeveloper.dependance.injector.api.exceptions;

public class InjectionNotFoundException extends ContainerException
{
    public InjectionNotFoundException() {
    }

    public InjectionNotFoundException(String message) {
        super(message);
    }

    public InjectionNotFoundException(String message, Object... values) {
        super(message, values);
    }

    public InjectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectionNotFoundException(Throwable cause) {
        super(cause);
    }

    public InjectionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
