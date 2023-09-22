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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultContainer implements Container, Registrable {
    protected final EventHandler eventHandler;
    protected final InstanceProvider instaneProvider;
    protected final Logger logger;
    protected final Map<Class<?>, InjectionInfo> injections;
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

        for (var registration : registrationInfos) {
            register(registration);
        }
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


    @Override
    public Object find(Class<?> _injection, Type... genericParameters) {
        try {
            var injectionInfo = injections.get(_injection);
            if (injectionInfo == null)
            {
                var onInjectionEvent = new OnInjectionEvent(_injection,
                        genericParameters,
                        injectionInfo,
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
