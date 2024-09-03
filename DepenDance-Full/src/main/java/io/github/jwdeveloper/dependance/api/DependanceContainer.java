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
package io.github.jwdeveloper.dependance.api;

import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;

import java.lang.reflect.Executable;
import java.lang.reflect.Type;

public interface DependanceContainer extends Container {

    /**
     * Finds an instance of the specified type that was previously registered in the container.
     *
     * @param <T>  the type of the instance to be retrieved
     * @param type the Class object of the type to be retrieved
     * @return an instance of the specified type, or null if not found
     */
    <T> T find(Class<T> type);

    /**
     * Resolves an array of object instances based on the provided parameter types.
     *
     * @param parametersTypes an array of Type objects representing the types of the parameters to be resolved
     * @return an array of object instances corresponding to the specified parameter types
     */
    Object[] resolveParameters(Type... parametersTypes);

    /**
     * Resolves and returns the parameters required for the given method or constructor.
     *
     * @param method an Executable object representing the method or constructor whose parameters are to be resolved
     * @return an array of object instances representing the resolved parameters for the specified method or constructor
     */
    Object[] resolveParameters(Executable method);


    /**
     * Create object instance based on the provided parameters and the types inside the container
     *
     * @param type output object type
     * @param args input object parameter
     * @return object instance
     */
    Object resolveObject(Class<?> type, Object... args);

    /**
     * Creates a builder for a sub-container that will be a child of this container.
     *
     * @return a DependanceContainerBuilder for building a sub-container
     */
    DependanceContainerBuilder createChildContainer();
}
