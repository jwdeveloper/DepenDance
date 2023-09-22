package io.github.jwdeveloper.dependance.implementation;

import io.github.jwdeveloper.dependance.api.events.AutoScanEvent;
import io.github.jwdeveloper.dependance.implementation.common.JarScanner;
import io.github.jwdeveloper.dependance.injector.api.annotations.IgnoreInjection;
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;
import io.github.jwdeveloper.dependance.injector.api.containers.builders.ContainerBuilder;
import io.github.jwdeveloper.dependance.injector.implementation.containers.ContainerConfigurationImpl;

import java.util.ArrayList;
import java.util.List;
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


        for (var _class : classes) {
            if (registeredTypes.contains(_class) ||
                    _class.isAnnotationPresent(IgnoreInjection.class) ||
                    _class.isInterface()) {
                continue;
            }
            registerType(_class);
        }

        var autoScanEvents  = new AutoScanEvent();
        for(var event : dependecyContainerConfig.getAutoScanEvents())
        {
            event.accept(autoScanEvents);
        }
        return toInitializeTypes;
    }

    private void registerType(Class<?> _class) {
        var injectionInfo = _class.getAnnotation(Injection.class);

        if (!injectionInfo.lazyLoad())
            toInitializeTypes.add(_class);

        var interfaces = _class.getInterfaces();
        if (interfaces.length == 0 || injectionInfo.ignoreInterface()) {
            containerBuilder.register(_class, injectionInfo.lifeTime());
            return;
        }

        if (injectionInfo.toInterface().equals(Object.class)) {
            containerBuilder.register((Class) interfaces[0], _class, injectionInfo.lifeTime());
            return;
        }

        containerBuilder.register((Class) injectionInfo.toInterface(), _class, injectionInfo.lifeTime());
    }
}
