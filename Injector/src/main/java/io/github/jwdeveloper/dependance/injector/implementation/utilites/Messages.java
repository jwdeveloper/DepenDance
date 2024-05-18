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
package io.github.jwdeveloper.dependance.injector.implementation.utilites;

public class Messages
{
    public static final String INJECTION_NOT_FOUND ="Class %s has not been register to Dependency Injection container";
    public static final String INJECTION_NOT_FOUND_GENERICS_TYPE ="Not found class %s with generic type %s in Dependency Injection container";


    public static final String INJECTION_ALREADY_EXISTS = "Class %s has been already register inside Dependency Injection container";
    public static final String INJECTION_CANT_BE_CREATED = "Can not create instance injection of type type %s";
    public static final String INJECTION_CANT_REGISTER = "Can not register injection of type type %s";

    public static final String INJECTION_LIST_WITH_INTERFACE = "Only Interface can be use as List parameter %s";
    public static final String INJECTION_USE_ANNOTATION_WITH_MORE_CONSTUROCTORS = "You need to use annotation Inject.class while there is more then one constructor";
    public static final String INJECTION_DETECTED_CYCLE_DEPENDECY = "Detected cycle dependency for type %s in class constructor %s";

    public static final String INJECTION_LIST_ALLOWED_TYPES = "Implementation must be an Interface or Abstract class";
}
