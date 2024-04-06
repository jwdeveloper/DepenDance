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
package common;


import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.implementation.DependanceContainerBuilder;
import io.github.jwdeveloper.dependance.injector.api.containers.Container;
import org.junit.Assert;
import org.junit.Before;


public abstract class ContainerTestBase {
    public static DependanceContainerBuilder builder;

    @Before
    public  void before() {
        builder = Dependance.newContainer();
    }


    protected void shouldBeNull(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        Assert.assertNull(instance1);
    }

    protected void shouldBeNotNull(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        Assert.assertNotNull(instance1);
    }

    protected void shouldBeDifferentInstance(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        var instance2 = container.find(clazz);
        Assert.assertNotEquals(instance1, instance2);
    }

    protected void shouldBeEqualsInstance(Container container, Class<?> clazz) {
        var instance1 = container.find(clazz);
        var instance2 = container.find(clazz);
        Assert.assertEquals(instance1, instance2);
    }

    protected void shouldBeEqualsInstance(Container container, Class<?> clazz, Object instance) {
        var instance1 = container.find(clazz);
        Assert.assertEquals(instance, instance1);
    }

    protected void shouldThrows(Class<? extends Throwable> throwablClass, Runnable action)
    {
        try
        {
            action.run();
        }
        catch (Exception e)
        {
            if(throwablClass.equals(e.getClass()))
            {
                return;
            }

            Throwable cause = null;
            do
            {
                 cause = e.getCause();

                 if(cause.getClass().equals(throwablClass))
                 {
                     return;
                 }
            }
            while (cause != null);

            throw e;
        }

    }
}
