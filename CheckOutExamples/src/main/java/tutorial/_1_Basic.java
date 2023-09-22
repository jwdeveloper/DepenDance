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

import tutorial.models.Config;
import tutorial.models.LocalShop;
import tutorial.models.Shop;
import tutorial.models.ShopManager;
import io.github.jwdeveloper.dependance.Dependance;
import org.junit.Assert;

public class _1_Basic {
    public static void main(String[] args)
    {
        /*
           - Singleton There will be only one instance of object created by container
           - Transient everytime `container.find` is used new instance is created
         */

        var container = Dependance.newContainer()
                .registerTransient(Shop.class, LocalShop.class) //registration interface to class
                .registerSingleton(Config.class)
                .registerSingleton(ShopManager.class)

                //auto-register all classes with annotation @injection
                // that are in same package as input
                .autoRegistration(_1_Basic.class)
                .build();


        var shopManager1 = (ShopManager)container.find(ShopManager.class);
        var shopManager2 = (ShopManager)container.find(ShopManager.class);

        Assert.assertEquals(shopManager1, shopManager2);
        Assert.assertEquals(shopManager1.getConfig(), shopManager2.getConfig());
        Assert.assertNotEquals(shopManager1.getShop(), shopManager2.getShop());
    }
}
