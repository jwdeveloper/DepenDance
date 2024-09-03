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
import io.github.jwdeveloper.dependance.injector.api.annotations.Injection;
import org.junit.Assert;

public class _10_Methods
{
    public static void main(String[] args) {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_10_Methods.ExampleSerivce.class)
                .registerTransient(_10_Methods.ExampleClass.class)
                .build();

        _10_Methods.ExampleClass example = container.find(_10_Methods.ExampleClass.class);
        Assert.assertNotNull(example);
        example.sayIt();
    }

    public static class ExampleSerivce
    {
        @Injection
        _09_Fields.ExampleClass exampleClassProvider()
        {
            return new _09_Fields.ExampleClass();
        }
    }

    public static class ExampleClass {
        public void sayIt() {
            System.out.println("Hello world!");
        }
    }
}
