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

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.api.DependanceContainerConfiguration;
import io.github.jwdeveloper.dependance.api.JarScanner;
import io.github.jwdeveloper.dependance.decorator.api.builder.DecoratorBuilder;
import io.github.jwdeveloper.dependance.decorator.implementation.DecoratorBuilderImpl;
import io.github.jwdeveloper.dependance.implementation.common.JarScannerImpl;
import io.github.jwdeveloper.dependance.implementation.common.JarScannerOptions;
import io.github.jwdeveloper.dependance.implementation.common.ScannerEvent;
import io.github.jwdeveloper.dependance.injector.implementation.containers.builder.ContainerBuilderImpl;
import io.github.jwdeveloper.dependance.injector.implementation.factory.InjectionInfoFactoryImpl;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DependanceContainerBuilder extends ContainerBuilderImpl<DependanceContainerConfiguration, DependanceContainerBuilder> {

    private final DecoratorBuilder decoratorBuilder;
    private final JarScannerOptions options;
    private List<JarScanner> additionalScanners = new ArrayList<>();
    private boolean scanEnabled;

    @Getter
    private final DependanceContainerConfigurationImpl dependanceContainerConfiguration;

    public DependanceContainerBuilder() {
        super();
        this.decoratorBuilder = new DecoratorBuilderImpl(new InjectionInfoFactoryImpl(), new HashMap<>());
        this.options = new JarScannerOptions();
        dependanceContainerConfiguration = new DependanceContainerConfigurationImpl(this.getConfiguration());
    }

    public <T> DependanceContainerBuilder registerDecorator(Class<T> _interface, Class<? extends T> implementation) {
        decoratorBuilder.decorate(_interface, implementation);
        return this;
    }

    @Override
    public DependanceContainerBuilder configure(Consumer<DependanceContainerConfiguration> consumer) {
        consumer.accept(dependanceContainerConfiguration);
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

    public DependanceContainerBuilder scan(JarScanner jarScanner) {
        scanEnabled = true;
        this.additionalScanners.add(jarScanner);
        return this;
    }

    public DependanceContainerBuilder scan(Class packageClass, ScannerEvent event) {
        return scan(x ->
        {
            x.includePackage(packageClass, event);
        });
    }

    @Override
    public DependanceContainer build() {
        var decorator = decoratorBuilder.build();
        configure(config -> config.onEvent(decorator));

        if (!scanEnabled) {
            return new DepenDanceContainerImpl(super.build());
        }
        var jarScanner = new JarScannerImpl(options, Logger.getLogger(Dependance.class.getSimpleName()));
        for (var additionalScanner : additionalScanners) {
            jarScanner.addClasses(additionalScanner.findAll());
        }
        var scanner = new InjectionsScanner(this, options, jarScanner);
        var toInitialize = scanner.scanAndRegister();
        var container = super.build();
        for (var clazz : toInitialize) {
            container.find(clazz);
        }
        return new DepenDanceContainerImpl(container);
    }
}
