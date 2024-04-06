/*
 * Copyright (c) 2023-2023 jwdeveloper  <jacekwoln@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.jwdeveloper.dependance.injector.implementation.containers.builder;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.containers.ContainerConfiguration;
import io.github.jwdeveloper.dependance.injector.api.containers.builders.ContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.enums.RegistrationType;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;
import io.github.jwdeveloper.dependance.injector.implementation.containers.DefaultContainer;
import io.github.jwdeveloper.dependance.injector.implementation.events.EventHandlerImpl;
import io.github.jwdeveloper.dependance.injector.implementation.factory.InjectionInfoFactoryImpl;
import io.github.jwdeveloper.dependance.injector.implementation.provider.InstanceProviderImpl;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class ContainerBuilderImpl<Config extends ContainerConfiguration, Builder extends ContainerBuilder<Config, Builder>> implements ContainerBuilder<Config, Builder> {
    protected final ContainerConfigurationImpl config;
    protected final Logger logger;

    public ContainerBuilderImpl(Logger logger) {
        config = new ContainerConfigurationImpl();
        this.logger = logger;
    }

    public ContainerBuilderImpl() {
        this(Logger.getLogger(DefaultContainer.class.getSimpleName()));
    }


    public ContainerConfigurationImpl getConfiguration() {
        return config;
    }


    @Override
    public Builder configure(Consumer<Config> configuration) {
        configuration.accept((Config) config);
        return builder();
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
    public <T> Builder registerSingletonList(Class<T> _interface,  Function<Container, Object> provider) {
        config.addRegistration(new RegistrationInfo(
                List.class,
                _interface,
                provider,
                LifeTime.SINGLETON,
                RegistrationType.List
        ));
        return builder();
    }

    @Override
    public <T> Builder registerTransientList(Class<T> _interface,  Function<Container, Object> provider) {
        config.addRegistration(new RegistrationInfo(
                List.class,
                _interface,
                provider,
                LifeTime.TRANSIENT,
                RegistrationType.List
        ));
        return builder();
    }

    @Override
    public Builder registerSingletonList(Class<?> genericType) {
        return registerList(genericType, LifeTime.SINGLETON);
    }

    @Override
    public Builder registerTransientList(Class<?> genericType) {
        return registerList(genericType, LifeTime.TRANSIENT);
    }

    @Override
    public <T> Builder registerList(Class<T> genericType, LifeTime lifeTime) {
        return registerList(genericType, lifeTime, null);
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
        return register(_interface, LifeTime.SINGLETON, (x) -> instance);
    }

    @Override
    public Builder registerSingleton(Class<?> _interface, Function<Container, Object> provider) {
        return register(_interface, LifeTime.SINGLETON, provider);
    }

    @Override
    public Builder registerTransient(Class<?> _interface, Function<Container, Object> provider) {
        return register(_interface, LifeTime.TRANSIENT, provider);
    }

    private Builder builder() {
        return (Builder) this;
    }

    private void addRegisteredType(Class<?> type) {
        config.getRegisterdTypes().add(type);
    }

    public Container build() {
        var eventHandler = new EventHandlerImpl(config.getEvents());
        var instanceProvider = new InstanceProviderImpl();
        var injectionInfoFactory = new InjectionInfoFactoryImpl();


 /*       return new DefaultContainer(
                searchAgent,
                instanceProvider,
                eventHandler,
                logger,
                injectionInfoFactory,
                config.getRegistrations());*/


        return new DefaultContainer(
                instanceProvider,
                eventHandler,
                logger,
                injectionInfoFactory,
                config.getRegistrations());
    }
}
