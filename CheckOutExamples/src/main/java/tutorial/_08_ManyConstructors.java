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
package tutorial;

import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.api.DependanceContainer;
import io.github.jwdeveloper.dependance.injector.api.annotations.Inject;
import org.junit.Assert;

public class _08_ManyConstructors
{
    /**
     *  By the default the first constructor is always targeted for injecting parameters
     *  However, sometimes class can have more than one constructor, or we want to use
     *  specific one.
     *
     *  To do that use @Inject annotation over wanted constructor
     *
     */

    public static void main(String[] args) {

        DependanceContainer container = Dependance.newContainer()
                .registerTransient(ExampleClass.class)
                .registerTransient(ManyConstructorsExample.class)
                .build();

        ManyConstructorsExample example = container.find(ManyConstructorsExample.class);
        Assert.assertNotNull(example);
        System.out.println("It works!");
    }

    public static class ManyConstructorsExample
    {

        public ManyConstructorsExample(String a, int b, boolean c)
        {

        }

        @Inject
        public ManyConstructorsExample(ExampleClass c)
        {
            System.out.println("Hello from constructor with ExampleClass parameter");
        }
    }

    public static class ExampleClass
    {

    }
}
