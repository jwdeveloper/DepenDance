package io.github.jwdeveloper.dependance.injector.api.search;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface ContainerSearch
{
     <T> Collection<T> findAllByInterface(Class<T> _interface);

     <T> Collection<T> findAllBySuperClass(Class<T> superClass);

     Collection<Object> findAllByAnnotation(Class<? extends Annotation> _annotation);
}
