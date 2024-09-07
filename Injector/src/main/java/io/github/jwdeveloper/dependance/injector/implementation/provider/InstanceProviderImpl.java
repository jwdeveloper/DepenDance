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
package io.github.jwdeveloper.dependance.injector.implementation.provider;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.enums.RegistrationType;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.provider.InstanceProvider;


import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class InstanceProviderImpl implements InstanceProvider {
    @Override
    public Object getInstance(InjectionInfo info, Container container) throws Exception {
        if (info.getLifeTime() == LifeTime.SINGLETON && info.getInstance() != null)
            return info.getInstance();

        Object result = null;
        InjectionInfo handler = null;
        Class<?> parameterClass = null;
        Object source;
        if (info.hasInjectedConstructor()) {
            var i = 0;
            for (var parameterType : info.getInjectedConstructorTypes()) {
                parameterClass = parameterType;
                source = info.getInjectedConstructor().getParameters()[i];
                Type genericType = info.getInjectedConstructor().getGenericParameterTypes()[i];
                if (genericType instanceof ParameterizedType parameterizedType) {
                    info.getConstructorPayLoadTemp()[i] = container.find(parameterClass, source, parameterizedType.getActualTypeArguments());
                } else {
                    info.getConstructorPayLoadTemp()[i] = container.find(parameterClass, source, genericType);
                }
                i++;
            }
            result = info.getInjectedConstructor().newInstance(info.getConstructorPayLoadTemp());
            for (var injectedField : info.getInjectedFields()) {
                source = injectedField;
                var fieldValue = container.find(injectedField.getType(), source, injectedField.getGenericType());
                injectedField.set(result, fieldValue);
            }
            info.setInstance(result);
            return result;
        }

        result = switch (info.getRegistrationInfo().registrationType()) {
            case InterfaceAndIml, OnlyImpl -> info.getRegistrationInfo().implementation().newInstance();
            case InterfaceAndProvider -> info.getRegistrationInfo().provider().apply(container);
            case List -> handleList(info, container);
        };
        if (info.getRegistrationInfo().registrationType() == RegistrationType.InterfaceAndProvider) {
            info.setInjectionValueType(result.getClass());
        }

        info.setInstance(result);
        return result;
    }


    private List<Object> handleList(InjectionInfo info, Container container) {
        var listTargetType = info.getInjectionValueType();
        var search = container.findAllByInterface(listTargetType);
        return Arrays.asList(search.toArray());
    }
}
