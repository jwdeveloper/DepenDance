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

import io.github.jwdeveloper.dependance.api.events.AutoScanEvent;
import io.github.jwdeveloper.dependance.implementation.common.JarScanner;
import io.github.jwdeveloper.dependance.injector.api.annotations.IgnoreInjection;
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;
import io.github.jwdeveloper.dependance.injector.api.containers.builders.ContainerBuilder;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class InjectionInfoSearch {
    DependanceContainerBuilder containerBuilder;
    List<Class<?>> toInitializeTypes;
    Class<?> _package;

    public InjectionInfoSearch(ContainerBuilder containerBuilder, Class<?> clazz) {
        this.containerBuilder = (DependanceContainerBuilder) containerBuilder;
        this.toInitializeTypes = new ArrayList<>();
        this._package = clazz;
    }

    public List<Class<?>> scanAndRegister() {
        var scanner = new JarScanner(_package, Logger.getLogger(JarScanner.class.getSimpleName()));
        var classes = scanner.findByAnnotation(Injection.class);

        var dependecyContainerConfig = containerBuilder.getDependanceContainerConfiguration();
        var config = (ContainerConfigurationImpl)dependecyContainerConfig.getConfiguration();
        var registeredTypes = config.getRegisterdTypes();
        var autoScanEvents= dependecyContainerConfig.getAutoScanEvents();
        for (var _class : classes) {
            if (registeredTypes.contains(_class) ||
                    _class.isAnnotationPresent(IgnoreInjection.class) ||
                    _class.isInterface()) {
                continue;
            }
            registerType(_class, autoScanEvents);
        }


        return toInitializeTypes;
    }

    private void registerType(Class<?> _class, List<Function<AutoScanEvent,Boolean>> autoScanEvents) {
        var injection = _class.getAnnotation(Injection.class);

        var autoScanEvent  = new AutoScanEvent(_class, containerBuilder, injection);
        for(var event : autoScanEvents)
        {
           var eventResult = event.apply(autoScanEvent);
           if(!eventResult)
           {
               return;
           }
        }

        if (!injection.lazyLoad())
            toInitializeTypes.add(_class);

        var interfaces = _class.getInterfaces();
        if (interfaces.length == 0 || injection.ignoreInterface()) {
            containerBuilder.register(_class, injection.lifeTime());
            return;
        }

        if (injection.toInterface().equals(Object.class)) {
            containerBuilder.register((Class) interfaces[0], _class, injection.lifeTime());
            return;
        }

        containerBuilder.register((Class) injection.toInterface(), _class, injection.lifeTime());
    }
}
