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
package io.github.jwdeveloper.dependance.injector.implementation.factory;

import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import io.github.jwdeveloper.dependance.injector.api.exceptions.ContainerException;
import io.github.jwdeveloper.dependance.injector.api.exceptions.DeathCycleException;
import io.github.jwdeveloper.dependance.injector.api.factory.InjectionInfoFactory;
import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.api.util.Pair;
import io.github.jwdeveloper.dependance.injector.implementation.utilites.Messages;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class InjectionInfoFactoryImpl implements InjectionInfoFactory {

    public Pair<Class<?>, InjectionInfo> create(RegistrationInfo info) throws Exception {
        return switch (info.registrationType()) {
            case InterfaceAndIml -> InterfaceAndImlStrategy(info);
            case OnlyImpl -> OnlyImplStrategy(info);
            case InterfaceAndProvider -> InterfaceAndProviderStrategy(info);
            case List -> ListStrategy(info);
        };

    }

    private Pair<Class<?>, InjectionInfo> OnlyImplStrategy(RegistrationInfo info) throws Exception {
        var impl = info.implementation();
        if (Modifier.isAbstract(impl.getModifiers())) {
            throw new ContainerException("Abstract class can't be register to Injection " + impl.getName());
        }
        if (Modifier.isInterface(impl.getModifiers())) {
            throw new ContainerException("Implementation must be class, not Interface");
        }

        var result = new InjectionInfo();
        var constructor = getConstructor(impl);
        throwIfCycleDependency(constructor, impl, impl);

        var extentedTypes = getExtentedTypes(impl);
        var implementedTypes = getImplementedTypes(impl);
        var annotations = getAnnotations(impl, extentedTypes);
        result.setSuperClasses(extentedTypes);
        result.setInterfaces(implementedTypes);
        result.setAnnotations(annotations);

        result.setInjectedConstructor(constructor);
        result.setConstructorTypes(constructor.getParameterTypes());
        result.setRegistrationInfo(info);
        result.setInjectionKeyType(impl);
        result.setInjectionValueType(impl);
        return new Pair<>(impl, result);
    }

    private Pair<Class<?>, InjectionInfo> InterfaceAndImlStrategy(RegistrationInfo info) throws Exception {
        var impl = info.implementation();
        var _interface = info._interface();
        if (Modifier.isAbstract(impl.getModifiers())) {
            throw new ContainerException("Abstract class can't be register to Injection " + impl.getName());
        }
        if (Modifier.isInterface(impl.getModifiers())) {
            throw new ContainerException("Implementation must be class, not Interface");
        }

        var result = new InjectionInfo();
        var constructor = getConstructor(impl);
        throwIfCycleDependency(constructor, impl, impl);

        var extendedTypes = getExtentedTypes(impl);
        var implementedTypes = getImplementedTypes(impl);
        var annotations = getAnnotations(impl, extendedTypes);
        result.setSuperClasses(extendedTypes);
        result.setInterfaces(implementedTypes);
        result.setAnnotations(annotations);

        result.setInjectedConstructor(constructor);
        result.setConstructorTypes(constructor.getParameterTypes());
        result.setRegistrationInfo(info);
        result.setInjectionKeyType(_interface);
        result.setInjectionValueType(impl);
        return new Pair<>(_interface, result);
    }


    private Pair<Class<?>, InjectionInfo> InterfaceAndProviderStrategy(RegistrationInfo info) {
        var _interface = info._interface();
        var result = new InjectionInfo();
        result.setRegistrationInfo(info);
        result.setInjectionKeyType(_interface);
        result.setInjectionValueType(Function.class);
        return new Pair<>(_interface, result);
    }

    private Pair<Class<?>, InjectionInfo> ListStrategy(RegistrationInfo info) throws Exception {
        var _interface = info._interface();
        if (!Modifier.isInterface(_interface.getModifiers()) || !Modifier.isAbstract(_interface.getModifiers())) {
            throw new ContainerException(Messages.INJECTION_LIST_ALLOWED_TYPES);
        }

        var result = new InjectionInfo();
        result.setRegistrationInfo(info);
        result.setInjectionKeyType(_interface);
        result.setInjectionValueType(List.class);
        return new Pair<>(_interface, result);
    }


    private static Set<Class<?>> getImplementedTypes(Class<?> clazz) {
        Set<Class<?>> interfaces = new HashSet<>();

        Class<?>[] directInterfaces = clazz.getInterfaces();
        interfaces.addAll(Arrays.asList(directInterfaces));

        for (Class<?> directInterface : directInterfaces) {
            interfaces.addAll(getImplementedTypes(directInterface));
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            interfaces.addAll(getImplementedTypes(superClass));
        }

        return interfaces;
    }

    private Set<Class<? extends Annotation>> getAnnotations(Class<?> type, Set<Class<?>> parentTypes) {
        var annotations = new HashSet<Class<? extends Annotation>>();
        for (var annotation : type.getAnnotations()) {
            annotations.add(annotation.annotationType());
        }

        for (var parent : parentTypes) {
            for (var annotation : parent.getAnnotations()) {
                annotations.add(annotation.getClass());
            }
        }
        return annotations;
    }


    private Set<Class<?>> getExtentedTypes(Class<?> type) {
        var superClassTypes = new HashSet<Class<?>>();
        var subClass = type.getSuperclass();
        while (subClass != null && !subClass.equals(Object.class)) {
            superClassTypes.add(subClass);
            subClass = subClass.getSuperclass();
        }
        return superClassTypes;
    }

    private Constructor getConstructor(Class<?> _class) {
        var consturctors = _class.getConstructors();
        if (consturctors.length == 1) {
            return consturctors[0];
        }

        for (var consturctor : consturctors) {
            if (!consturctor.isAnnotationPresent(Inject.class))
                continue;

            return consturctor;
        }
        throw new ContainerException(Messages.INJECTION_USE_ANNOTATION_WITH_MORE_CONSTUROCTORS);
    }

    private void throwIfCycleDependency(Constructor constructor, Class<?> current, Class<?> root) {
        for (var param : constructor.getParameterTypes()) {
            if (param.equals(root)) {
                throw new DeathCycleException(Messages.INJECTION_DETECTED_CYCLE_DEPENDECY, root.getSimpleName(), current.getSimpleName());
            }
            var ctr = getConstructor(param);
            throwIfCycleDependency(ctr, param, root);
        }
    }

}
