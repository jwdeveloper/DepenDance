package io.github.jwdeveloper.dependance.injector.api.events.events;

import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;

import java.lang.reflect.Type;
import java.util.Map;

public record OnInjectionEvent(Class<?> input,
                               Type[] inputGenericParameters,
                               InjectionInfo injectionInfo,
                               Object output,
                               Map<Class<?>, InjectionInfo> injectionInfoMap,
                               Container container)
{

    public boolean hasGenericParameters()
    {
        return inputGenericParameters != null && this.inputGenericParameters.length != 0;
    }

    public boolean hasOutput()
    {
        return this.output != null;
    }

    public boolean hasInjectionInfo()
    {
         return this.injectionInfo != null;
    }
}
