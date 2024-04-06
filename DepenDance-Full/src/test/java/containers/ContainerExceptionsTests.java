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
package containers;

import common.ContainerTestBase;
import common.classess.ExampleClass;
import common.classess.exceptionsExamples.ClassWithMoreConstructors;
import common.classess.exceptionsExamples.ClassWithParamterNotRegistered;
import common.classess.exceptionsExamples.deathcycle.A;
import common.classess.exceptionsExamples.deathcycle.B;
import common.classess.exceptionsExamples.deathcycle.C;
import common.classess.exceptionsExamples.deathcycle.CycleDependencyExample;
import io.github.jwdeveloper.dependance.injector.api.exceptions.ContainerException;
import io.github.jwdeveloper.dependance.injector.api.exceptions.DeathCycleException;
import io.github.jwdeveloper.dependance.injector.api.exceptions.InjectionNotFoundException;
import io.github.jwdeveloper.dependance.injector.implementation.utilites.Messages;
import org.junit.Assert;
import org.junit.Test;

public class ContainerExceptionsTests extends ContainerTestBase {
    @Test
    public void shouldReturnNullWhenNotRegistered() {
        shouldThrows(InjectionNotFoundException.class, () ->
        {
            var container = builder.build();
            container.find(ExampleClass.class);
        });
    }

    @Test
    public void shouldThrowWhenDeathCycle() {
        shouldThrows(DeathCycleException.class, () ->
        {
            builder.registerSingleton(CycleDependencyExample.class)
                    .registerSingleton(A.class)
                    .registerSingleton(B.class)
                    .registerSingleton(C.class)
                    .build();
        });
    }

    @Test
    public void shouldThrowWhenThereAreMoreConstructorsWithoutAnnotation() {
        Assert.assertThrows(Messages.INJECTION_USE_ANNOTATION_WITH_MORE_CONSTUROCTORS, ContainerException.class, () ->
        {
            builder.registerSingleton(ClassWithMoreConstructors.class)
                    .build();
        });
    }

    @Test
    public void shouldThrowsWhenClassHaveTypeInConstructorThatIsNotRegistered() {

        Assert.assertThrows(Messages.INJECTION_CANT_BE_CREATED, ContainerException.class, () ->
        {
            var container = builder.registerSingleton(ClassWithParamterNotRegistered.class)
                    .build();
            container.find(ClassWithParamterNotRegistered.class);
        });
    }
}
