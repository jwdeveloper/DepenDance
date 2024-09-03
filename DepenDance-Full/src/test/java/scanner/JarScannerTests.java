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
package scanner;

import common.ContainerTestBase;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;
import org.junit.Test;

public class JarScannerTests {

    public static class ExampleClass1 {

    }

    public static class ExampleClass2 {

    }

    @Test
    public void shouldFindAllClasses() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
        }).initialize();


        Assert.assertEquals(3, scanner.size());
    }

    @Test
    public void shouldIncludeAnotherPackage() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
            options.includePackage(ContainerTestBase.class);
        }).initialize();

        var result = scanner.size() > 3;
        Assert.assertTrue(result);
    }

    @Test
    public void shouldExcludeClass() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
            options.excludeClass(ExampleClass1.class);
        });

        var result = scanner.findAll();

        Assert.assertTrue(result.stream().filter(e -> e.equals(ExampleClass1.class)).findFirst().isEmpty());
    }

    @Test
    public void shouldExcludePackage() {
        var scanner = Dependance.newJarScanner(options ->
        {
            options.setRootPackage(JarScannerTests.class);
            options.excludePackage(ExampleClass1.class.getPackageName());
        });

        var result = scanner.findAll();
        Assert.assertEquals(0, result.size());
        Assert.assertTrue(result.stream().filter(e -> e.equals(ExampleClass1.class)).findFirst().isEmpty());
        Assert.assertTrue(result.stream().filter(e -> e.equals(ExampleClass2.class)).findFirst().isEmpty());
    }
}
