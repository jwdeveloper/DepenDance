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
package io.github.jwdeveloper.dependance.injector.implementation.containers;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.containers.Registrable;
import io.github.jwdeveloper.dependance.injector.api.events.EventHandler;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;
import io.github.jwdeveloper.dependance.injector.api.exceptions.ContainerException;
import io.github.jwdeveloper.dependance.injector.api.exceptions.InjectionNotFoundException;
import io.github.jwdeveloper.dependance.injector.api.factory.InjectionInfoFactory;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.api.provider.InstanceProvider;
import io.github.jwdeveloper.dependance.injector.api.search.SearchAgent;
import io.github.jwdeveloper.dependance.injector.implementation.utilites.Messages;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultContainer implements Container, Registrable {
    protected final EventHandler eventHandler;
    protected final InstanceProvider instaneProvider;
    protected final Logger logger;
    protected final Map<Class<?>, InjectionInfo> injections;
    protected final Map<Class<?>, Map<Class<?>,InjectionInfo>> injections2;
    protected final InjectionInfoFactory injectionInfoFactory;
    protected final SearchAgent searchAgent;

    public DefaultContainer(SearchAgent searchAgent,
                            InstanceProvider instaneProvider,
                            EventHandler eventHandler,
                            Logger logger,
                            InjectionInfoFactory injectionInfoFactory,
                            List<RegistrationInfo> registrationInfos) {
        this.instaneProvider = instaneProvider;
        this.eventHandler = eventHandler;
        this.logger = logger;
        this.injectionInfoFactory = injectionInfoFactory;
        this.searchAgent = searchAgent;
        this.injections = new HashMap<>();
        this.injections2 = new HashMap<>();

        for (var registration : registrationInfos) {
            register(registration);
            register2(registration);
        }
    }



    public boolean register2(RegistrationInfo registrationInfo) {
        try {
            var pair = injectionInfoFactory.create(registrationInfo);
            var registrationResult = eventHandler.OnRegistration(new OnRegistrationEvent(pair.getKey(), pair.getValue(), registrationInfo));
            if (!registrationResult) {
                return false;
            }
            injections2.putIfAbsent(pair.getKey(),new LinkedHashMap<>());
            injections2.computeIfPresent(pair.getKey(),(aClass, classInjectionInfoMap) ->
            {
               classInjectionInfoMap.put(pair.getValue().getInjectionValueType(), pair.getValue());
                return classInjectionInfoMap;
            });
        }
        catch (Exception exception)
        {
            throw new ContainerException(String.format(Messages.INJECTION_CANT_REGISTER, exception),exception);
        }
        return true;
    }

    @Override
    public boolean register(RegistrationInfo registrationInfo) {
        try {
            var pair = injectionInfoFactory.create(registrationInfo);
            var registrationResult = eventHandler.OnRegistration(new OnRegistrationEvent(pair.getKey(), pair.getValue(), registrationInfo));
            if (!registrationResult) {
                return false;
            }
            injections.put(pair.getKey(), pair.getValue());
        }
        catch (Exception exception)
        {
            throw new ContainerException(String.format(Messages.INJECTION_CANT_REGISTER, exception),exception);
        }
        return true;
    }



    public Object find2(Class<?> _injection, Type... genericParameters) {
        try {
            var injectionInfos = injections2.get(_injection);
            if (injectionInfos == null)
            {
                var onInjectionEvent = new OnInjectionEvent(_injection,
                        genericParameters,
                        null,
                        null,
                        injections,
                        this);
                var result = eventHandler.OnInjection(onInjectionEvent);
                if (result == null) {
                    throw new InjectionNotFoundException(Messages.INJECTION_NOT_FOUND, _injection.getSimpleName());
                }
                return result;
            }

            InjectionInfo injectionInfo  = null;
            if(genericParameters.length != 0)
            {
                injectionInfo = injectionInfos.get(genericParameters[0]);
            }

            var instance = instaneProvider.getInstance(injectionInfo, injections, this);
            var onInjectionEvent = new OnInjectionEvent(
                    _injection,
                    genericParameters,
                    injectionInfo,
                    instance,
                    injections,
                    this);
            return eventHandler.OnInjection(onInjectionEvent);
        }
        catch (Exception e)
        {
            throw new ContainerException(String.format(Messages.INJECTION_CANT_BE_CREATED, _injection.getSimpleName()),e);
        }
    }

    @Override
    public Object find(Class<?> _injection, Type... genericParameters) {
        try {
            var injectionInfo = injections.get(_injection);
            if (injectionInfo == null)
            {
                var onInjectionEvent = new OnInjectionEvent(_injection,
                        genericParameters,
                        null,
                        null,
                        injections,
                        this);
                var result = eventHandler.OnInjection(onInjectionEvent);
                if (result == null) {
                    throw new InjectionNotFoundException(Messages.INJECTION_NOT_FOUND, _injection.getSimpleName());
                }
                return result;
            }

            var instance = instaneProvider.getInstance(injectionInfo, injections, this);
            var onInjectionEvent = new OnInjectionEvent(
                    _injection,
                    genericParameters,
                    injectionInfo,
                    instance,
                    injections,
                    this);
            return eventHandler.OnInjection(onInjectionEvent);
        }
        catch (Exception e)
        {
            throw new ContainerException(String.format(Messages.INJECTION_CANT_BE_CREATED, _injection.getSimpleName()),e);
        }
    }


    @Override
    public <T> Collection<T> findAllByInterface(Class<T> _interface) {
        return searchAgent.<T>findAllByInterface(this::find, injections, _interface);
    }

    @Override
    public <T> Collection<T> findAllBySuperClass(Class<T> superClass) {
        return searchAgent.<T>findAllBySuperClass(this::find, injections, superClass);
    }

    @Override
    public Collection<Object> findAllByAnnotation(Class<? extends Annotation> _annotation) {
        return searchAgent.findAllByAnnotation(this::find, injections, _annotation);
    }
}
