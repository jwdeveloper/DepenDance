package io.github.jwdeveloper.dependacne.decorator.implementation;

import io.github.jwdeveloper.dependacne.decorator.api.DecoratorInstanceProvider;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.implementation.utilites.Messages;

import java.util.Map;

public class DecoratorInstanceProviderImpl implements DecoratorInstanceProvider {
    @Override
    public Object getInstance(InjectionInfo info, Map<Class<?>, InjectionInfo> injections, Object toSwap, Container container) {
        try {
            return tryGetInstance(info, injections, toSwap, container);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object tryGetInstance(InjectionInfo info, Map<Class<?>, InjectionInfo> injections, Object toSwap, Container container) throws Exception {
        if (info.getLifeTime() == LifeTime.SINGLETON && info.getInstnace() != null)
            return info.getInstnace();

        Object result = null;
        InjectionInfo handler = null;
        if (info.hasInjectedConstructor()) {
            var i = 0;
            for (var parameter : info.getConstructorTypes()) {
                if (!injections.containsKey(parameter)) {
                    throw new RuntimeException(String.format(Messages.INJECTION_NOT_FOUND, parameter.getTypeName(), info.getInjectionKeyType()));
                }
                handler = injections.get(parameter);
                if (info.getInjectionKeyType().equals(parameter)) {
                    info.getConstructorPayLoadTemp()[i] = toSwap;
                } else {
                    info.getConstructorPayLoadTemp()[i] = getInstance(handler, injections, toSwap, container);
                }

                i++;
            }
            result = info.getInjectedConstructor().newInstance(info.getConstructorPayLoadTemp());
            info.setInstnace(result);
            return result;
        }

        result = switch (info.getRegistrationInfo().registrationType()) {
            case InterfaceAndIml, OnlyImpl -> info.getRegistrationInfo().implementation().newInstance();
            case InterfaceAndProvider, List -> info.getRegistrationInfo().provider().apply(container);
        };
        info.setInstnace(result);
        return result;
    }
}
