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
import io.github.jwdeveloper.dependance.injector.api.enums.LifeTime;
import org.junit.Assert;
import tutorial.models.Config;
import tutorial.models.ExampleClass;
import tutorial.models.OnlineShop;


public class _6_AutoScan {

    /**
     * To avoid boring manually registering Types to container
     * use `scan` method that is looking for all Classes and Methods
     * with annotation @Injection and register it automatically
     */

    public static void main(String[] args) {

        /*
         *  package under which code will be scanned should be scanned
         *  scanner is looking for all Method and Classes that are having @Injection annotation
         */

        Class<?> rootClass = _6_AutoScan.class;

        DependanceContainer container = Dependance.newContainer()
                .scan(rootClass)
                .scan(options ->
                {
                    options.setRootPackage(rootClass);
                    options.addExcludedClass("org.example.ExampleClass");
                    options.addExcludePackage(String.class.getPackageName());
                })
                .build();

        Config config = container.find(Config.class);
        ExampleClass exampleClass = container.find(ExampleClass.class);
        OnlineShop onlineShop = container.find(OnlineShop.class);
        ExampleScannClass exampleScannClass = container.find(ExampleScannClass.class);

        Assert.assertNotNull(config);
        Assert.assertNotNull(exampleClass);
        Assert.assertNotNull(onlineShop);
        Assert.assertNotNull(exampleScannClass);
    }


    /**
     * This is equivalent of
     * <p>
     * container.registerTransient(OnlineShop.class,container1 ->
     * {
     * System.out.println("Hello from the online shop factory");
     * return new OnlineShop();
     * })
     */
    @Injection(lifeTime = LifeTime.TRANSIENT)
    private static OnlineShop onlineShopFactory() {
        System.out.println("Hello from the online shop factory");
        return new OnlineShop();
    }


    /**
     * This is equivalent of
     * <p>
     * container.registerSingleton(ExampleScannClass.class);
     */
    @Injection(lifeTime = LifeTime.SINGLETON)
    public static class ExampleScannClass {
        public ExampleScannClass() {
            System.out.println("Hello world!");
        }
    }
}
