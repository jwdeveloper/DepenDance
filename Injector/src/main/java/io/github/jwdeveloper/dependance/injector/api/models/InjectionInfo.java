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
package io.github.jwdeveloper.dependance.injector.api.models;

import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

@Data
public class InjectionInfo {
    private Class<?> injectionKeyType;
    private Class<?> injectionValueType;
    private RegistrationInfo registrationInfo;
    private Class<?>[] constructorTypes;
    private Constructor<?> injectedConstructor;
    private Set<Class<?>> superClasses = new HashSet<>();
    private Set<Class<?>> interfaces = new HashSet<>();
    private Set<Class<? extends Annotation>> annotations = new HashSet<>();

    private Object[] constructorPayLoadTemp;

    private Object instnace;

    public LifeTime getLifeTime() {
        return registrationInfo.lifeTime();
    }

    public boolean hasInjectedConstructor() {
        return injectedConstructor != null;
    }

    public boolean hasAnnotation(Class<? extends Annotation> _annotation) {
        return annotations.contains(_annotation);
    }

    public boolean hasSuperClass(Class<?> parent) {
        return superClasses.contains(parent);
    }

    public boolean hasInterface(Class<?> parent) {
        if (interfaces == null) {
            return false;
        }
        return interfaces.contains(parent);
    }

    public void setConstructorTypes(Class<?>[] constructorTypes) {
        this.constructorTypes = constructorTypes;
        this.constructorPayLoadTemp = new Object[constructorTypes.length];
    }
}
