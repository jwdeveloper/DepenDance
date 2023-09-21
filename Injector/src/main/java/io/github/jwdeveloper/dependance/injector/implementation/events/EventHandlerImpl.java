package io.github.jwdeveloper.dependance.injector.implementation.events;

import io.github.jwdeveloper.dependance.injector.api.events.ContainerEvents;
import io.github.jwdeveloper.dependance.injector.api.events.EventHandler;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;

import java.util.List;

public class EventHandlerImpl implements EventHandler {
    private final List<ContainerEvents> events;

    public EventHandlerImpl(List<ContainerEvents> events) {
        this.events = events;
    }

    @Override
    public boolean OnRegistration(OnRegistrationEvent event) {
        for (var handler : events) {
            var res = handler.OnRegistration(event);
            if (!res) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object OnInjection(OnInjectionEvent event) {
        var output = event.output();
        for (var handler : events) {
            output = handler.OnInjection(new OnInjectionEvent(event.input(),
                    event.inputGenericParameters(),
                    event.injectionInfo(),
                    output,
                    event.injectionInfoMap(),
                    event.container()));
        }
        return output;
    }
}
