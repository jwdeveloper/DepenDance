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

import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.api.DependanceContainerConfiguration;
import io.github.jwdeveloper.dependance.decorator.api.builder.DecoratorBuilder;
import io.github.jwdeveloper.dependance.decorator.implementation.DecoratorBuilderImpl;
import io.github.jwdeveloper.dependance.implementation.common.JarScannerOptions;
import io.github.jwdeveloper.dependance.injector.implementation.containers.builder.ContainerBuilderImpl;
import io.github.jwdeveloper.dependance.injector.implementation.factory.InjectionInfoFactoryImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.function.Consumer;

public class DependanceContainerBuilder extends ContainerBuilderImpl<DependanceContainerConfiguration, DependanceContainerBuilder> {

    private final DecoratorBuilder decoratorBuilder;
    private final JarScannerOptions options;
    private boolean scanEnabled;

    @Getter
    private final DependanceContainerConfigurationImpl dependanceContainerConfiguration;

    public DependanceContainerBuilder() {
        super();
        this.decoratorBuilder = new DecoratorBuilderImpl(new InjectionInfoFactoryImpl(), new HashMap<>());
        this.options = new JarScannerOptions();
        dependanceContainerConfiguration = new DependanceContainerConfigurationImpl(this.getConfiguration());
    }

    @Override
    public DependanceContainerBuilder configure(Consumer<DependanceContainerConfiguration> consumer) {
        consumer.accept(dependanceContainerConfiguration);
        return this;
    }

    public <T> DependanceContainerBuilder registerDecorator(Class<T> _interface, Class<? extends T> implementation) {
        decoratorBuilder.decorate(_interface, implementation);
        return this;
    }

    public DependanceContainerBuilder scan(Class<?> root) {
        return scan(x ->
        {
           x.setRootPackage(root);
        });
    }

    public DependanceContainerBuilder scan(Consumer<JarScannerOptions> consumer) {
        consumer.accept(options);
        scanEnabled = true;
        return this;
    }


    @Override
    public DependanceContainer build() {
        var decorator = decoratorBuilder.build();
        configure(config -> config.onEvent(decorator));

        if (!scanEnabled) {
            return new DepenDanceContainerImpl(super.build());
        }

        var search = new InjectionInfoSearch(this, options);
        var toInitialize = search.scanAndRegister();
        var container = super.build();
        for (var clazz : toInitialize) {
            container.find(clazz);
        }
        return new DepenDanceContainerImpl(container);
    }
}
