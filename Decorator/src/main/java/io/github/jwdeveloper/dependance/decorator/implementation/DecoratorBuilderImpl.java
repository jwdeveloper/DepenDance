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
import io.github.jwdeveloper.dependance.decorator.api.builder.DecoratorBuilder;
import io.github.jwdeveloper.dependance.decorator.api.models.ProxyData;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.api.enums.RegistrationType;
import io.github.jwdeveloper.dependance.injector.api.factory.InjectionInfoFactory;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecoratorBuilderImpl implements DecoratorBuilder {

    private final Map<Class<?>, List<RegistrationInfo>> registeredTypes;

    public DecoratorBuilderImpl() {
        this.registeredTypes = new HashMap<>();
    }

    @Override
    public <T> DecoratorBuilder registerProxy(Class<T> target, Class<? extends T> proxy) {
        var registrations = registeredTypes.computeIfAbsent(target, aClass -> new ArrayList<>());
        var registration = new RegistrationInfo(target,
                proxy,
                null,
                LifeTime.SESSION,
                RegistrationType.InterfaceAndIml);
        registrations.add(registration);
        return this;
    }

    @Override
    public Decorator build() {
        try {
            var proxies = createProxies();
            return new DefaultDecorator(proxies);
        } catch (Exception e) {
            throw new RuntimeException("Unable to build decorator", e);
        }
    }

    private Map<Class<?>, ProxyData> createProxies() {
        var decorators = new HashMap<Class<?>, ProxyData>();
        for (var registration : registeredTypes.entrySet()) {
            var targetType = registration.getKey();
            var targetValues = registration.getValue();

            var decorationDto = new ProxyData(targetType, targetValues);
            decorators.put(targetType, decorationDto);
        }
        return decorators;
    }
}
