package io.github.jwdeveloper.dependance.injector.api.containers;

import io.github.jwdeveloper.dependance.injector.api.events.ContainerEvents;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;

import java.util.function.Function;

public interface ContainerConfiguration {

    void onInjection(Function<OnInjectionEvent, Object> event);
    void onRegistration(Function<OnRegistrationEvent, Boolean> event);
    void onEvent(ContainerEvents event);
}
