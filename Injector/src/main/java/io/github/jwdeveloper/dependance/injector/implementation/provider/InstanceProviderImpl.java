package io.github.jwdeveloper.dependance.injector.implementation.provider;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.provider.InstanceProvider;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class InstanceProviderImpl implements InstanceProvider
{
    @Override
    public Object getInstance(InjectionInfo info, Map<Class<?>, InjectionInfo> injections, Container container) throws Exception {
        if (info.getLifeTime() == LifeTime.SINGLETON && info.getInstnace() != null)
            return info.getInstnace();

        Object result = null;
        InjectionInfo handler = null;
        Class<?> parameterClass = null;
        if (info.hasInjectedConstructor()) {
            var i = 0;
            for (var parameterType : info.getConstructorTypes())
            {
                parameterClass = parameterType;

                Type genericType = info.getInjectedConstructor().getGenericParameterTypes()[i];
                if(genericType instanceof ParameterizedType parameterizedType)
                {
                    info.getConstructorPayLoadTemp()[i] = container.find(parameterClass, parameterizedType.getActualTypeArguments());
                }
                else
                {
                    info.getConstructorPayLoadTemp()[i] = container.find(parameterClass, genericType);
                }
                i++;
            }
            result = info.getInjectedConstructor().newInstance(info.getConstructorPayLoadTemp());
            info.setInstnace(result);
            return result;
        }

        result = switch (info.getRegistrationInfo().registrationType())
        {
            case InterfaceAndIml, OnlyImpl -> info.getRegistrationInfo().implementation().newInstance();
            case InterfaceAndProvider, List -> info.getRegistrationInfo().provider().apply(container);
        };
        info.setInstnace(result);
        return result;
    }
}
