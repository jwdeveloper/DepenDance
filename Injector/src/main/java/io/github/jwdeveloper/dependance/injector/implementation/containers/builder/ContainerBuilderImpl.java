package io.github.jwdeveloper.dependance.injector.implementation.containers.builder;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.containers.builders.ContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.containers.builders.ContainerBuilderConfiguration;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.enums.RegistrationType;
import io.github.jwdeveloper.dependance.injector.api.exceptions.ContainerException;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.api.search.ContainerSearch;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;
import io.github.jwdeveloper.dependance.injector.implementation.containers.DefaultContainer;
import io.github.jwdeveloper.dependance.injector.implementation.events.EventHandlerImpl;
import io.github.jwdeveloper.dependance.injector.implementation.factory.InjectionInfoFactoryImpl;
import io.github.jwdeveloper.dependance.injector.implementation.provider.InstanceProviderImpl;
import io.github.jwdeveloper.dependance.injector.implementation.search.SearchAgentImpl;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class ContainerBuilderImpl<Builder extends ContainerBuilder<Builder>> implements ContainerBuilder<Builder>, ContainerBuilderConfiguration {
    protected final ContainerConfigurationImpl config;
    protected final Logger logger;

    public ContainerBuilderImpl(Logger logger) {
        config = new ContainerConfigurationImpl();
        this.logger = logger;
    }

    public ContainerBuilderImpl()
    {
        this(Logger.getLogger(DefaultContainer.class.getSimpleName()));
    }


    public ContainerConfigurationImpl getConfiguration() {
        return config;
    }


    public Builder register(RegistrationInfo registrationInfo) {
        config.addRegistration(registrationInfo);
        addRegisteredType(registrationInfo.implementation());
        return builder();
    }


    public Builder register(Class<?> implementation, LifeTime lifeTime) {
        config.addRegistration(new RegistrationInfo(
                null,
                implementation,
                null,
                lifeTime,
                RegistrationType.OnlyImpl
        ));
        addRegisteredType(implementation);
        return builder();
    }


    public <T> Builder register(Class<T> _interface, Class<? extends T> implementation, LifeTime lifeTime) {
        config.addRegistration(new RegistrationInfo(
                _interface,
                implementation,
                null,
                lifeTime,
                RegistrationType.InterfaceAndIml
        ));
        return builder();
    }


    @Override
    public <T> Builder registerList(Class<T> _interface, LifeTime lifeTime) {
        return registerList(_interface, lifeTime, (x) ->
        {
            var container = (ContainerSearch) x;
            var instances = container.findAllByInterface(_interface);
            return new ArrayList(instances);
        });
    }


    @Override
    public <T> Builder registerList(Class<T> _interface, LifeTime lifeTime, Function<Container, Object> provider) {
        config.addRegistration(new RegistrationInfo(
                List.class,
                _interface,
                provider,
                lifeTime,
                RegistrationType.List
        ));
        return builder();
    }

    @Override
    public Builder registerSingletonList(Class<?> _interface) {
        return registerList(_interface, LifeTime.SINGLETON);
    }

    @Override
    public Builder registerTransientList(Class<?> _interface) {
        return registerList(_interface, LifeTime.TRANSIENT);
    }


    @Override
    public <T> Builder register(Class<T> _interface, LifeTime lifeTime, Function<Container, Object> provider) {
        config.addRegistration(new RegistrationInfo(
                _interface,
                null,
                provider,
                lifeTime,
                RegistrationType.InterfaceAndProvider
        ));
        addRegisteredType(_interface);
        return builder();
    }

    private void addRegisteredType(Class<?> type) {
        if (config.getRegisterdTypes().contains(type)) {
            throw new ContainerException("Type " + type.getSimpleName() + " has been already registered to container");
        }
        config.getRegisterdTypes().add(type);
    }

    public <T> Builder registerSingleton(Class<T> _interface, Class<? extends T> implementation) {
        return register(_interface, implementation, LifeTime.SINGLETON);
    }


    public <T> Builder registerTransient(Class<T> _interface, Class<? extends T> implementation) {
        return register(_interface, implementation, LifeTime.TRANSIENT);
    }

    public Builder registerSingleton(Class<?> implementation) {
        return register(implementation, LifeTime.SINGLETON);
    }

    public Builder registerTransient(Class<?> implementation) {
        return register(implementation, LifeTime.TRANSIENT);
    }

    public Builder registerSingleton(Class<?> _interface, Object instance) {
        return register(_interface, LifeTime.SINGLETON, (x) ->   instance);
    }

    @Override
    public Builder registerSingleton(Class<?> _interface, Function<Container, Object> provider) {
        return register(_interface, LifeTime.SINGLETON, provider);
    }

    @Override
    public Builder registerTransient(Class<?> _interface, Function<Container, Object> provider) {
        return register(_interface, LifeTime.TRANSIENT, provider);
    }

    @Override
    public Builder configure(Consumer<ContainerConfigurationImpl> configuration) {
        configuration.accept(config);
        return builder();
    }

    private Builder builder() {
        return (Builder) this;
    }

    public Container build()
    {
        var eventHandler = new EventHandlerImpl(config.getEvents());
        var instanceProvider = new InstanceProviderImpl();
        var injectionInfoFactory = new InjectionInfoFactoryImpl();
        var searchAgent = new SearchAgentImpl();

        return new DefaultContainer(
                searchAgent,
                instanceProvider,
                eventHandler,
                logger,
                injectionInfoFactory,
                config.getRegistrations());
    }
}
