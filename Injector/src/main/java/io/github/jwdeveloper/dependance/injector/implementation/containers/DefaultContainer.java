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
import java.util.logging.Logger;


public class DefaultContainer implements Container, Registrable {
    protected final EventHandler eventHandler;
    protected final InstanceProvider instaneProvider;
    protected final Logger logger;
    protected final Map<Class<?>, List<InjectionInfo>> injections;
    protected final InjectionInfoFactory injectionInfoFactory;

    public DefaultContainer(
            InstanceProvider instanceProvider,
            EventHandler eventHandler,
            Logger logger,
            InjectionInfoFactory injectionInfoFactory,
            List<RegistrationInfo> registrationInfos) {
        this.instaneProvider = instanceProvider;
        this.eventHandler = eventHandler;
        this.logger = logger;
        this.injectionInfoFactory = injectionInfoFactory;
        this.injections = new HashMap<>();

        for (var registration : registrationInfos) {
            register(registration);
        }
    }


    public boolean register(RegistrationInfo registrationInfo) {
        try {
            var injectionCreateResult = injectionInfoFactory.create(registrationInfo);
            var onRegisterEvent = new OnRegistrationEvent(
                    injectionCreateResult.getKey(),
                    injectionCreateResult.getValue(),
                    registrationInfo);

            var registrationResult = eventHandler.OnRegistration(onRegisterEvent);
            if (!registrationResult) {
                return false;
            }
            injections.putIfAbsent(injectionCreateResult.getKey(), new ArrayList<>());
            injections.computeIfPresent(injectionCreateResult.getKey(), (type, list) ->
            {
                list.add(injectionCreateResult.getValue());
                return list;
            });
        } catch (Exception exception) {

            var clazz = "";
            if (registrationInfo._interface() != null) {
                clazz += registrationInfo._interface().getName();
            }
            if (registrationInfo._interface() != null) {
                clazz += " " + registrationInfo.implementation().getName();
            }
            throw new ContainerException(String.format(Messages.INJECTION_CANT_REGISTER, clazz), exception);
        }
        return true;
    }

    public Object find(Class<?> _injection, Type... genericParameters) {
        var injectionInfos = injections.get(_injection);
        if (injectionInfos == null) {
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

        if (genericParameters == null || genericParameters.length == 0) {
            var lastInjectionInfo = injectionInfos.get(injectionInfos.size() - 1);
            return find(lastInjectionInfo, genericParameters);
        }

        var genericsType = genericParameters[0];
        var optional = injectionInfos.stream().filter(e -> e.getInjectionValueType().equals(genericsType)).findFirst();
        if (optional.isEmpty()) {
            throw new InjectionNotFoundException(Messages.INJECTION_NOT_FOUND_GENERICS_TYPE, _injection.getSimpleName(), genericsType.getTypeName());
        }
        return find(optional.get(), genericParameters);
    }

    private Object find(InjectionInfo injectionInfo, Type... genericParameters) {
        var injectionType = injectionInfo.getInjectionKeyType();
        try {
            var instance = instaneProvider.getInstance(injectionInfo, this);
            var onInjectionEvent = new OnInjectionEvent(
                    injectionType,
                    genericParameters,
                    injectionInfo,
                    instance,
                    injections,
                    this);
            return eventHandler.OnInjection(onInjectionEvent);
        } catch (Exception e) {
            throw new ContainerException(String.format(Messages.INJECTION_CANT_BE_CREATED, injectionType.getSimpleName()), e);
        }
    }


    @Override
    public <T> Collection<T> findAllByInterface(Class<T> _interface) {

        Class _searchedInterface = _interface;
        Object temp = null;
        var result = new ArrayList<T>();
        for (var entry : injections.entrySet()) {
            for (var injection : entry.getValue()) {
                if (injection.hasInterface(_searchedInterface)) {
                    temp = find(injection.getInjectionKeyType());
                    result.add((T) temp);
                }
            }
        }
        return result;
    }

    @Override
    public <T> Collection<T> findAllBySuperClass(Class<T> superClass) {
        var result = new ArrayList<>();
        for (var set : injections.entrySet()) {
            for (var injection : set.getValue()) {
                if (!injection.hasSuperClass(superClass))
                    continue;
                result.add(find(set.getKey()));
            }
        }
        return (List<T>) result;
    }

    @Override
    public Collection<Object> findAllByAnnotation(Class<? extends Annotation> _annotation) {
        var result = new ArrayList<>();
        for (var set : injections.entrySet()) {
            for (var injection : set.getValue()) {
                if (!injection.hasAnnotation(_annotation))
                    continue;
                result.add(find(set.getKey()));
            }
        }
        return result;
    }


}