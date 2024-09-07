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

import io.github.jwdeveloper.dependance.decorator.api.Decorator;
import io.github.jwdeveloper.dependance.decorator.api.annotations.ProxyProvider;
import io.github.jwdeveloper.dependance.decorator.api.models.ProxyData;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnInjectionEvent;
import io.github.jwdeveloper.dependance.injector.api.events.events.OnRegistrationEvent;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class DefaultDecorator implements Decorator {
    private final Map<Class<?>, ProxyData> targetToProxiesMap;
    private final Map<Class<?>, RegistrationInfo> proxyToTargetMap;

    public DefaultDecorator(Map<Class<?>, ProxyData> decorators) {
        this.targetToProxiesMap = decorators;
        this.proxyToTargetMap = new HashMap<>();

        for (var value : decorators.values()) {
            for (var proxy : value.proxies()) {
                proxyToTargetMap.put(proxy.implementation(), proxy);
            }
        }

    }

    @Override
    public boolean OnRegistration(OnRegistrationEvent event) {
        return true;
    }

    public Object OnInjection(OnInjectionEvent event) {
        var output = event.output();
        var target = event.input();
        if (proxyToTargetMap.containsKey(event.input())) {
            var proxy = proxyToTargetMap.get(event.input());
            target = proxy._interface();
            return event.container().find(target, event.source());
        }

        var proxyData = targetToProxiesMap.get(event.input());
        if (proxyData == null) {
            return output;
        }
        if (output == null) {
            return null;
        }


        for (var proxy : proxyData.proxies()) {
            var targetObject = output;
            output = createProxyInstance(targetObject, proxy, event.container());
            setProxyFieldObject(targetObject, output);
        }
        return output;
    }


    private void setProxyFieldObject(Object target, Object proxy) {
        if (target.getClass().isInterface()) {
            return;
        }
        for (var field : target.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(ProxyProvider.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(target, proxy);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object createProxyInstance(Object target, RegistrationInfo injectionInfo, Container container) {
        var classLoader = this.getClass().getClassLoader();
        var targetType = injectionInfo._interface();
        var proxyType = injectionInfo.implementation();
        var proxyObject = Proxy.newProxyInstance(
                classLoader,
                new Class<?>[]{proxyType},
                (inputInstance, method, arguments) ->
                {
                    if (method.isDefault()) {
                        return InvocationHandler.invokeDefault(inputInstance, method, arguments);
                    }

                    Method targetTypeMethod = null;
                    try {
                        targetTypeMethod = targetType.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                        //pass
                    }

                    if (!targetTypeMethod.getReturnType().equals(method.getReturnType())) {
                        var result = targetTypeMethod.invoke(target, arguments);
                        var info = new RegistrationInfo(result.getClass(), method.getReturnType(), null, null, null);
                        return createProxyInstance(result, info, container);
                    }

                    if (targetTypeMethod != null) {
                        return targetTypeMethod.invoke(target, arguments);
                    }

                    throw new UnsupportedOperationException("Method not supported: " + method.getName());
                });
        return proxyObject;
    }
}
