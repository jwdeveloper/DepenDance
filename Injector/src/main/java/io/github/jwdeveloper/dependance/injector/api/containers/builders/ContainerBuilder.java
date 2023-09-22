package io.github.jwdeveloper.dependance.injector.api.containers.builders;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.containers.ContainerConfiguration;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ContainerBuilder<Config extends ContainerConfiguration,  Builder extends ContainerBuilder<Config,Builder>> {

    Builder configure(Consumer<Config> configuration);

    Builder register(RegistrationInfo registrationInfo);

    Builder register(Class<?> _class, LifeTime lifeTime);

    <T> Builder register(Class<T> _interface, Class<? extends T> implementation, LifeTime lifeTime);

    <T> Builder register(Class<T> _interface, LifeTime lifeTime, Function<Container, Object> provider);

    <T> Builder registerList(Class<T> _interface, LifeTime lifeTime);

    <T> Builder registerList(Class<T> _interface, LifeTime lifeTime, Function<Container, Object> provider);

    Builder registerSingletonList(Class<?> _interface);

    Builder registerTransientList(Class<?> _interface);

    Builder registerSingleton(Class<?> _class);

    Builder registerTransient(Class<?> _class);

    <T> Builder registerSingleton(Class<T> _interface, Class<? extends T> implementation);

    <T> Builder registerTransient(Class<T> _interface, Class<? extends T> implementation);

    Builder registerSingleton(Class<?> _interface, Object instance);
    Builder registerSingleton(Class<?> _interface, Function<Container, Object> provider);

    Builder registerTransient(Class<?> _interface, Function<Container, Object> provider);
}
