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
package io.github.jwdeveloper.dependance.injector.implementation.search;

import io.github.jwdeveloper.dependance.injector.api.models.InjectionInfo;
import io.github.jwdeveloper.dependance.injector.api.search.SearchAgent;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class SearchAgentImpl implements SearchAgent {


    public <T> Collection<T> findAllByInterface(Function<Class<?>, Object> find, Map<Class<?>, InjectionInfo> injections, Class<T> _interface) {
        var result = new ArrayList<T>();
        for (var set : injections.entrySet()) {
            var injection = set.getValue();
            if (injection.getInjectionKeyType().isAssignableFrom(_interface)) {
                result.add((T) find.apply(set.getKey()));
                continue;
            }

            if (!injection.hasInterface(_interface))
                continue;

            result.add((T) find.apply(set.getKey()));
        }
        return result;
    }

    public <T> Collection<T> findAllBySuperClass(Function<Class<?>, Object> find, Map<Class<?>, InjectionInfo> injections, Class<T> superClass) {
        var result = new ArrayList<T>();
        for (var set : injections.entrySet()) {
            var injection = set.getValue();
            if (!injection.hasSuperClass(superClass))
                continue;

            result.add((T) find.apply(set.getKey()));
        }
        return result;
    }

    public Collection<Object> findAllByAnnotation(Function<Class<?>, Object> find, Map<Class<?>, InjectionInfo> injections, Class<? extends Annotation> _annotation) {
        var result = new ArrayList<>();
        for (var set : injections.entrySet()) {
            var injection = set.getValue();
            if (!injection.hasAnnotation(_annotation))
                continue;

            result.add(find.apply(set.getKey()));
        }
        return result;
    }
}
