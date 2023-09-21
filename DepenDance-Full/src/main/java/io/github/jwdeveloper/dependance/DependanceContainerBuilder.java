package io.github.jwdeveloper.dependance;

import io.github.jwdeveloper.dependacne.decorator.api.builder.DecoratorBuilder;
import io.github.jwdeveloper.dependacne.decorator.implementation.DecoratorBuilderImpl;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.implementation.containers.builder.ContainerBuilderImpl;
import io.github.jwdeveloper.dependance.injector.implementation.factory.InjectionInfoFactoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DependanceContainerBuilder extends ContainerBuilderImpl<DependanceContainerBuilder> {

    private final DecoratorBuilder decoratorBuilder;
    private final List<Class<?>> classesToInitialize;
    public DependanceContainerBuilder() {
        this.decoratorBuilder = new DecoratorBuilderImpl(new InjectionInfoFactoryImpl(), new HashMap<>());
        classesToInitialize = new ArrayList<>();
    }

    public <T> DependanceContainerBuilder registerDecorator(Class<T> _interface, Class<? extends T> implementation) {
        decoratorBuilder.decorate(_interface, implementation);
        return this;
    }

    public DependanceContainerBuilder autoRegistration(Class<?> root)
    {
        var search = new InjectionInfoSearch(this,root);
        classesToInitialize.addAll(search.scanAndRegister());
        return this;
    }

    @Override
    public Container build()
    {
        var decorator = decoratorBuilder.build();
        configure(config -> config.onEvent(decorator));

        configure(config -> config.onInjection(e ->
        {
            if(!e.input().isAssignableFrom(List.class))
            {
                return e.output();
            }
            if(e.inputGenericParameters().length == 0)
            {
                throw new RuntimeException("There should be generic parameter provided for list");
            }
            var parameter = e.inputGenericParameters()[0];

            try
            {
                var clazz = Class.forName(parameter.getTypeName());
                var instances = e.container().findAllByInterface(clazz);
                return instances.stream().toList();
            }
            catch (Exception ex)
            {
                return new ArrayList<>();
            }
        }));
        var container= super.build();
        for(var clazz : classesToInitialize)
        {
            container.find(clazz);
        }
        return container;
    }
}
