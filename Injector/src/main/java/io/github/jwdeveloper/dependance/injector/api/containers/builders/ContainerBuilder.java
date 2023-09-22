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
