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

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import io.github.jwdeveloper.dependance.injector.implementation.containers.DefaultContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class DepenDanceContainerImpl implements DependanceContainer {

    private final Container container;

    public DepenDanceContainerImpl(Container container) {
        this.container = container;
    }

    public <T> T find(Class<T> type) {
        return (T) container.find(type);
    }

    @Override
    public Object[] resolveParameters(Type[] types) {
        var output = new Object[types.length];
        var index = 0;
        for (var type : types) {
            if (type instanceof ParameterizedType parameterizedType) {
                output[index] = find((Class<?>) parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
            } else {
                output[index] = find((Class<?>) type);
            }
            index++;
        }
        return output;
    }

    @Override
    public Object[] resolveParameters(Executable executable) {
        var parameters = executable.getParameters();
        var types = Arrays.stream(parameters)
                .map(Parameter::getParameterizedType)
                .toArray(Type[]::new);
        return resolveParameters(types);
    }

    @Override
    public Object resolveObject(Class<?> type, Object... args) {
        return null;
    }


    /**
     * Creates sub container that is linked with the current container
     *
     * @return the sub-container builder
     */
    @Override
    public DependanceContainerBuilder createChildContainer() {
        return Dependance.newContainer().linkContainer(this);
    }


    public DependanceContainerBuilder createSession() {
        var sessionContainer = createChildContainer();

        if (container instanceof DefaultContainer defaultContainer) {
            var injections = defaultContainer
                    .injections()
                    .values()
                    .stream()
                    .mapMulti((list, downstream) ->
                            list.stream()
                                    .filter(x -> x.getLifeTime().equals(LifeTime.SESSION))
                                    .forEach(downstream)
                    )
                    .toList();

        }
        return sessionContainer;
    }



    @Override
    public Object find(Class<?> type, Type... genericParameters) {
        return container.find(type, genericParameters);
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
