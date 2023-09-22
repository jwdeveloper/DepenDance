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
package io.github.jwdeveloper.dependance.decorator.implementation;

import io.github.jwdeveloper.dependance.decorator.api.DecoratorInstanceProvider;
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
