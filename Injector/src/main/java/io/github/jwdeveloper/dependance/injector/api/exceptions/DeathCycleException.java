package io.github.jwdeveloper.dependance.injector.api.exceptions;

public class DeathCycleException extends ContainerException
{
    public DeathCycleException() {
    }

    public DeathCycleException(String message) {
        super(message);
    }

    public DeathCycleException(String message, Object... values) {
        super(message, values);
    }

    public DeathCycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeathCycleException(Throwable cause) {
        super(cause);
    }

    public DeathCycleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
