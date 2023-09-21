package io.github.jwdeveloper.dependance.injector.api.containers.builders;

import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;

import java.util.function.Consumer;

public interface ContainerBuilderConfiguration
{
    public ContainerBuilder configure(Consumer<ContainerConfigurationImpl> configuration);
}
