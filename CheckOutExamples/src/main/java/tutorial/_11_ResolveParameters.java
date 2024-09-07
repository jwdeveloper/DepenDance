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

public class _11_ResolveParameters {
    public static void main(String[] args) throws Exception {
        DependanceContainer container = Dependance.newContainer()
                .registerTransient(_11_ResolveParameters.ExampleWithGenerics.class)
                .registerTransient(_11_ResolveParameters.ExampleClass.class)
                .configure(config ->
                {
                    config.onInjection(onInjectionEvent ->
                    {
                        if (!onInjectionEvent.input().equals(ExampleWithGenerics.class)) {
                            return onInjectionEvent.output();
                        }

                        var geneicsTyles = onInjectionEvent.inputGenericParameters()[0];
                        if (!String.class.equals(geneicsTyles)) {
                            return onInjectionEvent.output();
                        }
                        return new ExampleWithGenerics<String>();
                    });
                })
                .build();

        var method = _11_ResolveParameters.class.getDeclaredMethod(
                "sayHello",
                _11_ResolveParameters.ExampleWithGenerics.class,
                _11_ResolveParameters.ExampleClass.class);

        var parameters = container.resolveParameters(method);

        method.invoke(null, parameters);
    }


    public static void sayHello(_11_ResolveParameters.ExampleWithGenerics<String> exampleService,
                                _11_ResolveParameters.ExampleClass exampleClass) {

        System.out.println("Hello world");
    }


    public static class ExampleWithGenerics<T> {

    }

    public static class ExampleClass {

    }
}
