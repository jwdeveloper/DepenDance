package io.github.jwdeveloper.dependance.injector.api.search;

import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface SearchAgent
{
     <T> Collection<T> findAllByInterface(Function<Class<?>,Object> find, Map<Class<?>, InjectionInfo> injections, Class<T> _interface) ;

     <T> Collection<T> findAllBySuperClass(Function<Class<?>,Object> find, Map<Class<?>, InjectionInfo> injections, Class<T> superClass);

     Collection<Object> findAllByAnnotation(Function<Class<?>,Object> find, Map<Class<?>, InjectionInfo> injections, Class<? extends Annotation> _annotation);
}
