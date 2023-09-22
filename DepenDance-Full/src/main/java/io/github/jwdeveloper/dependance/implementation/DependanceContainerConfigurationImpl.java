package io.github.jwdeveloper.dependance.implementation;

import io.github.jwdeveloper.dependance.api.DependanceContainerConfiguration;
import io.github.jwdeveloper.dependance.api.events.AutoScanEvent;
import io.github.jwdeveloper.dependance.injector.api.containers.ContainerConfiguration;
import io.github.jwdeveloper.dependance.injector.api.events.ContainerEvents;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DependanceContainerConfigurationImpl implements DependanceContainerConfiguration
{
    @Getter
    private final ContainerConfiguration configuration;

    @Getter
    private final List<Consumer<AutoScanEvent>> autoScanEvents;

    public DependanceContainerConfigurationImpl(ContainerConfiguration configuration) {
        this.configuration = configuration;
        autoScanEvents = new ArrayList<>();
    }

    @Override
    public void onAutoScan(Consumer<AutoScanEvent> event) {
        autoScanEvents.add(event);
    }

    @Override
    public void onInjection(Function<OnInjectionEvent, Object> event) {
        configuration.onInjection(event);
    }

    @Override
    public void onRegistration(Function<OnRegistrationEvent, Boolean> event) {
        configuration.onRegistration(event);
    }

    @Override
    public void onEvent(ContainerEvents event) {
        configuration.onEvent(event);
    }
}
