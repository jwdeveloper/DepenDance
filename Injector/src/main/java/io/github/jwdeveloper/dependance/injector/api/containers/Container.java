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
package io.github.jwdeveloper.dependance.injector.api.containers;

import io.github.jwdeveloper.dependance.injector.api.models.RegistrationInfo;
import io.github.jwdeveloper.dependance.injector.api.search.ContainerSearch;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface Container extends Cloneable, ContainerSearch {
    /**
     * Look for the object inside the container by the type
     * When object is not found it throws Exception
     *
     * @param type              the search type
     * @param genericParameters the generics parameters of the search type, they could be empty
     * @return the instance of found type object
     */
    Object find(Class<?> type, Type... genericParameters);


    /**
     * Search for the object inside the container by the type
     * When object is not found it throws Exception
     *
     * @param type              the search type
     * @param source            the member source, (Constructor parameter, Type field) that is currently being resolved
     * @param genericParameters the generics parameters of the search type, they could be empty
     * @return the instance of found type object
     */
    Object find(Class<?> type, Object source, Type... genericParameters);
}
