package io.github.jwdeveloper.dependacne.decorator.api.builder;


import io.github.jwdeveloper.dependacne.decorator.api.Decorator;

public interface DecoratorBuilder {
    <T> DecoratorBuilder decorate(Class<T> _interface, Class<? extends T> implementation);
    Decorator build() ;
}
