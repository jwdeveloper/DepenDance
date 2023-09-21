package io.github.jwdeveloper.dependacne.decorator.api;


import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;

import java.util.Map;

public interface DecoratorInstanceProvider
{
    Object getInstance(InjectionInfo info, Map<Class<?>, InjectionInfo> injections, Object toSwap, Container container) ;

}
