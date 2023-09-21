package io.github.jwdeveloper.dependance.injector.api.search;

import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface SearchAgent
{
    public <T> Collection<T> findAllByInterface(Function<Class<?>,Object> find, Map<Class<?>, InjectionInfo> injections, Class<T> _interface) ;

    public <T> Collection<T> findAllBySuperClass(Function<Class<?>,Object> find, Map<Class<?>, InjectionInfo> injections, Class<T> superClass);

    public Collection<Object> findAllByAnnotation(Function<Class<?>,Object> find, Map<Class<?>, InjectionInfo> injections, Class<? extends Annotation> _annotation);
}
