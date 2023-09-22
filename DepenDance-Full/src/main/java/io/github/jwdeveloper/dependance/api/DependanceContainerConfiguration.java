package io.github.jwdeveloper.dependance.api;

import io.github.jwdeveloper.dependance.api.events.AutoScanEvent;
import io.github.jwdeveloper.dependance.injector.api.containers.ContainerConfiguration;
import java.util.function.Consumer;


public interface DependanceContainerConfiguration extends ContainerConfiguration
{
    void onAutoScan(Consumer<AutoScanEvent> event);
}
