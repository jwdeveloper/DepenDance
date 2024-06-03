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
package io.github.jwdeveloper.dependance.containers;

import io.github.jwdeveloper.dependance.ContainerBenchmark;
import io.github.jwdeveloper.dependance.Dependance;
import io.github.jwdeveloper.dependance.exampleClasses.*;

public class DepenDanceContainer {
    public static void run() {
        var container = Dependance.newContainer()
                .registerTransient(ExampleClass.class)
                .registerTransient(ExampleClass2.class)
                .registerTransient(ExampleClass3.class)
                .registerTransient(ExampleClass4.class)
                .registerTransient(ExampleClass5.class)
                .registerTransient(ExampleClass6.class)
                .build();
        container.find(ExampleClass.class);
    }

    public static void main(String[] args) throws InterruptedException {

        var now = System.nanoTime();
        var iteration = 0;
        while (true) {
            now = System.nanoTime();
            run();
            var endTime = System.nanoTime();
            var delta = endTime - now;
            System.out.println(iteration + " Action performed in " + delta / 1_000_000f + " MS");
            Thread.sleep(10);
            iteration++;
        }
    }
}
