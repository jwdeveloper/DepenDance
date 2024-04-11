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
package io.github.jwdeveloper.dependance.implementation;

import io.github.jwdeveloper.dependance.api.JarScanner;
import io.github.jwdeveloper.dependance.api.events.AutoScanEvent;
import io.github.jwdeveloper.dependance.implementation.common.JarScannerImpl;
import io.github.jwdeveloper.dependance.implementation.common.JarScannerOptions;
import io.github.jwdeveloper.dependance.injector.api.annotations.IgnoreInjection;
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;
import io.github.jwdeveloper.dependance.injector.api.containers.builders.ContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.exceptions.ContainerException;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InjectionsScanner {
    DependanceContainerBuilder containerBuilder;
    List<Class<?>> toInitializeTypes;
    JarScannerOptions options;
    JarScanner jarScanner;

    public InjectionsScanner(ContainerBuilder containerBuilder, JarScanner jarScanner) {
        this.containerBuilder = (DependanceContainerBuilder) containerBuilder;
        this.toInitializeTypes = new ArrayList<>();
        this.jarScanner = jarScanner;
    }

    public List<Class<?>> scanAndRegister()
    {
        jarScanner.onPackageScan((_package, classes) ->
        {
            if (!options.getScannerEvents().containsKey(_package)) {
                return;
            }
            options.getScannerEvents().get(_package).onScanned(classes, containerBuilder);
        });
        jarScanner.initialize();
        var methods = findMethods(jarScanner.findAll());
        var classes = findClasses(jarScanner.findAll());

        var containerConfig = containerBuilder.getDependanceContainerConfiguration();
        var config = (ContainerConfigurationImpl) containerConfig.getConfiguration();
        var registeredTypes = config.getRegisterdTypes();
        var autoScanEvents = containerConfig.getAutoScanEvents();
        for (var _class : classes) {
            if (registeredTypes.contains(_class)) {
                continue;
            }
            registerType(_class, autoScanEvents);
        }

        for (var method : methods) {
            if (registeredTypes.contains(method.getReturnType())) {
                continue;
            }
            registerMethod(method, autoScanEvents);
        }

        return toInitializeTypes;
    }

    private void registerMethod(Method method, List<Function<AutoScanEvent, Boolean>> autoScanEvents) {
        var injection = method.getAnnotation(Injection.class);
        var clazz = method.getReturnType();
        var autoScanEvent = new AutoScanEvent(clazz, containerBuilder, injection);
        for (var event : autoScanEvents) {
            var eventResult = event.apply(autoScanEvent);
            if (!eventResult) {
                return;
            }
        }

        if (!injection.lazyLoad())
            toInitializeTypes.add(clazz);

        method.setAccessible(true);
        containerBuilder.register(clazz, injection.lifeTime(), container ->
        {
            try {
                var params = new Object[method.getParameterCount()];
                var i = 0;
                for (var paramType : method.getParameterTypes()) {
                    params[i] = container.find(paramType);
                }
                return method.invoke(null, params);
            } catch (Exception e) {
                throw new ContainerException(e);
            }
        });
    }

    private void registerType(Class<?> clazz, List<Function<AutoScanEvent, Boolean>> autoScanEvents) {
        var injection = clazz.getAnnotation(Injection.class);
        var autoScanEvent = new AutoScanEvent(clazz, containerBuilder, injection);
        for (var event : autoScanEvents) {
            var eventResult = event.apply(autoScanEvent);
            if (!eventResult) {
                return;
            }
        }

        if (!injection.lazyLoad())
            toInitializeTypes.add(clazz);

        var interfaces = clazz.getInterfaces();
        if (interfaces.length == 0 || injection.ignoreInterface()) {
            containerBuilder.register(clazz, injection.lifeTime());
            return;
        }

        if (injection.toInterface().equals(Object.class)) {
            containerBuilder.register((Class) interfaces[0], clazz, injection.lifeTime());
            return;
        }

        containerBuilder.register((Class) injection.toInterface(), clazz, injection.lifeTime());
    }

    private Set<Method> findMethods(Collection<Class<?>> classes) {
        return classes
                .stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .filter(e -> Modifier.isStatic(e.getModifiers()) &&
                        !e.isAnnotationPresent(IgnoreInjection.class) &&
                        e.isAnnotationPresent(Injection.class))
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> findClasses(Collection<Class<?>> classes) {
        return classes
                .stream()
                .filter(e ->
                        !e.isInterface() &&
                                !e.isAnnotationPresent(IgnoreInjection.class) &&
                                e.isAnnotationPresent(Injection.class))
                .collect(Collectors.toSet());
    }
}
