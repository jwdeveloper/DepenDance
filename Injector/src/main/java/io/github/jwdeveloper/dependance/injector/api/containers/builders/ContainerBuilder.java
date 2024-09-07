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
package io.github.jwdeveloper.dependance.injector.api.containers.builders;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.containers.ContainerConfiguration;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;

import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Nomenclature
 * <p>
 * Request - obtaining instance of object from the container.
 * For example if we want request the Cat class we do.
 * ```
 * Cat cat = container.find(Cat.class);
 * ```
 */
public interface ContainerBuilder<Config extends ContainerConfiguration, Builder extends ContainerBuilder<Config, Builder>> {

    /**
     * Configure the container options, you can
     * set there things such as container events
     *
     * @param configuration the lambda action
     * @return the builder instance.
     */
    Builder configure(Consumer<Config> configuration);


    /**
     * @param registrationInfo
     * @return the builder instance
     */
    Builder register(RegistrationInfo registrationInfo);

    /**
     * Register given type into the container,
     * the LifeType might be adjusted
     *
     * @param type     the registration type
     * @param lifeTime the registration LifeTime
     * @return the builder instance
     * @see LifeTime
     */
    Builder register(Class<?> type, LifeTime lifeTime);

    /**
     * Register given interface and its implementation into the container.
     * When the interface is requested, the instance of the returned object
     * will be made from the type of the implementation.
     *
     * @param _interface     the interface
     * @param implementation the implementation of given interface
     * @param lifeTime       the type lifetime
     * @param <T>            The Type of Interface
     * @return the builder instance
     */
    <T> Builder register(Class<T> _interface, Class<? extends T> implementation, LifeTime lifeTime);


    /**
     * Register given interface and its provider into the container.
     * When the interface is requested, the provider will be called
     * and return the instance.
     * <p>
     * In case of LifeTime transient the provider will be called every request
     *
     * @param type     the registered type, can be Interface, Class, Record, Enum
     * @param provider the lambda provider with Container as input and Object instance as output
     * @param lifeTime the type lifetime
     * @param <T>      The Type of Interface
     * @return the builder instance
     */
    <T> Builder register(Class<T> type, LifeTime lifeTime, Function<Container, Object> provider);


    /**
     * Registers a List of the given type to the container. List will
     * be created by searching types in the container that are equal or assignable
     * for the given type.
     * <p>
     * You must use it as List<T>, however Collection or other implementation of the list are
     * not supported
     *
     * @param type     the type of the items that will be included to the List
     * @param lifeTime the LifeTime
     * @param <T>      The type of type
     * @return the builder instance
     */
    <T> Builder registerList(Class<T> type, LifeTime lifeTime);

    /**
     * Registers a List of the given type to the container as singleton. List will
     * be created by searching types in the container that are equal or assignable
     * for the given type.
     * <p>
     * You must use it as List<T>, however Collection or other implementation of the list are
     * not supported
     *
     * @param type the type of the items that will be included to the List
     * @return the builder instance
     */
    Builder registerSingletonList(Class<?> type);

    /**
     * Registers a List of the given type to the container as transient. List will
     * be created by searching types in the container that are equal or assignable
     * for the given type.
     * <p>
     * You must use it as List<T>, however Collection or other implementation of the list are
     * not supported
     *
     * @param type the type of the items that will be included to the List
     * @return the builder instance
     */
    Builder registerTransientList(Class<?> type);

    <T> Builder registerList(Class<T> type, LifeTime lifeTime, Function<Container, Object> provider);

    <T> Builder registerSingletonList(Class<T> type, Function<Container, Object> provider);

    <T> Builder registerTransientList(Class<T> type, Function<Container, Object> provider);

    Builder registerSingleton(Class<?> type);

    Builder registerTransient(Class<?> type);

    /**
     * Register given interface and its implementation into the container as singleton.
     * If interface was not requested even once, the instance of the returned object
     *
     * @param _interface     the interface
     * @param implementation the implementation of given interface
     * @param <T>            The Type of Interface
     * @return the builder instance
     */
    <T> Builder registerSingleton(Class<T> _interface, Class<? extends T> implementation);

    /**
     * Register given interface and its implementation into the container as transient.
     * Everytime the interface is requested, the instance of the returned object
     *
     * @param _interface     the interface
     * @param implementation the implementation of given interface
     * @param <T>            The Type of Interface
     * @return the builder instance
     */
    <T> Builder registerTransient(Class<T> _interface, Class<? extends T> implementation);


    /**
     * Register given type and its instance into the container as singleton.
     * Everytime interface is requested the instance is returned
     *
     * @param type     the type of instance
     * @param instance the instance of the type
     * @return the builder instance
     */
    Builder registerSingleton(Class<?> type, Object instance);

    /**
     * Register given type and its lambda provider into the container as singleton.
     * If interface was not requested even once, the provided is called and its provided
     * object is returned
     *
     * @param type     the type of the returned object of provider
     * @param provider the lambda provider with Container as input and Object instance as output
     * @return the builder instance
     */
    Builder registerSingleton(Class<?> type, Function<Container, Object> provider);

    /**
     * Register given type and its lambda provider into the container as transient.
     * Everytime the interface is requested,  the provided is called and its provided
     * object is returned
     *
     * @param type the interface
     * @param provider   the lambda provider with Container as input and Object instance as output
     * @return the builder instance
     */
    Builder registerTransient(Class<?> type, Function<Container, Object> provider);
}
