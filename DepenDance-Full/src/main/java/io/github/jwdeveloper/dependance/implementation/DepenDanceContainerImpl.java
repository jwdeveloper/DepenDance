package io.github.jwdeveloper.dependance.implementation;

import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

public class DepenDanceContainerImpl implements DependanceContainer
{

    private final Container container;

    public DepenDanceContainerImpl(Container container) {
        this.container = container;
    }

    public <T> T find(Class<T> type)
    {
        return (T)container.find(type);
    }


    @Override
    public Object find(Class<?> type, Type... genericParameters) {
        return container.find(type,genericParameters);
    }

    @Override
    public <T> Collection<T> findAllByInterface(Class<T> _interface) {
        return container.findAllByInterface(_interface);
    }

    @Override
    public <T> Collection<T> findAllBySuperClass(Class<T> superClass) {
        return container.findAllBySuperClass(superClass);
    }

    @Override
    public Collection<Object> findAllByAnnotation(Class<? extends Annotation> _annotation) {
        return container.findAllByAnnotation(_annotation);
    }
}
